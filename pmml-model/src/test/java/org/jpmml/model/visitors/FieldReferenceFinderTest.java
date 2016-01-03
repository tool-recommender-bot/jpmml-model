/*
 * Copyright (c) 2015 Villu Ruusmann
 */
package org.jpmml.model.visitors;

import java.io.InputStream;
import java.util.Set;

import javax.xml.transform.stream.StreamSource;

import org.dmg.pmml.FieldName;
import org.dmg.pmml.Model;
import org.dmg.pmml.PMML;
import org.dmg.pmml.Segment;
import org.dmg.pmml.Visitor;
import org.dmg.pmml.VisitorAction;
import org.jpmml.model.FieldNameUtil;
import org.jpmml.model.JAXBUtil;
import org.jpmml.model.PMMLUtil;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class FieldReferenceFinderTest {

	@Test
	public void find() throws Exception {
		PMML pmml;

		try(InputStream is = PMMLUtil.getResourceAsStream(FieldResolverTest.class)){
			pmml = JAXBUtil.unmarshalPMML(new StreamSource(is));
		}

		Visitor visitor = new AbstractVisitor(){

			@Override
			public VisitorAction visit(Segment segment){
				Model model = segment.getModel();

				String id = segment.getId();

				if("first".equals(id)){
					checkFields(FieldNameUtil.create("x1_squared"), model);
				} else

				if("second".equals(id)){
					checkFields(FieldNameUtil.create("x2", "x2_squared"), model);
				} else

				if("third".equals(id)){
					checkFields(FieldNameUtil.create("x3"), model);
				} else

				if("sum".equals(id)){
					checkFields(FieldNameUtil.create("first_output", "second_output", "third_output"), model);
				} else

				{
					throw new AssertionError();
				}

				return super.visit(segment);
			}
		};

		visitor.applyTo(pmml);
	}

	static
	private void checkFields(Set<FieldName> names, Model model){
		FieldReferenceFinder finder = new FieldReferenceFinder();
		finder.applyTo(model);

		assertEquals(names, finder.getFieldNames());
	}
}