/*
 * Copyright (c) 2019 Villu Ruusmann
 */
package org.dmg.pmml.tree;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.List;

import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import org.dmg.pmml.Extension;
import org.dmg.pmml.False;
import org.dmg.pmml.True;
import org.jpmml.model.JAXBUtil;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class NodeTest {

	@Test
	public void jaxbClone() throws Exception {
		Node node1a = new BranchNode()
			.setId("1a")
			.setPredicate(new True());

		List<Node> nodes = node1a.getNodes();

		Node node2a = new ComplexNode()
			.setId("2a")
			.addExtensions(new Extension())
			.setPredicate(new False());

		nodes.add(node2a);

		Node node2b = new LeafNode()
			.setId("2b")
			.setPredicate(new True());

		nodes.add(node2b);

		TreeModel treeModel = new TreeModel()
			.setNode(node1a);

		TreeModel jaxbTreeModel = (TreeModel)clone(treeModel);

		Node jaxbNode1a = jaxbTreeModel.getNode();

		assertEquals(node1a.getClass(), jaxbNode1a.getClass());
		assertEquals(node1a.getId(), jaxbNode1a.getId());

		List<Node> jaxbNodes = jaxbNode1a.getNodes();

		assertEquals(2, jaxbNodes.size());

		Node jaxbNode2a = jaxbNodes.get(0);

		assertEquals(node2a.getClass(), jaxbNode2a.getClass());
		assertEquals(node2a.getId(), jaxbNode2a.getId());

		assertTrue(jaxbNode2a.hasExtensions());

		Node jaxbNode2b = jaxbNodes.get(1);

		assertEquals(node2b.getClass(), jaxbNode2b.getClass());
		assertEquals(node2b.getId(), jaxbNode2b.getId());
	}

	static
	private Object clone(Object object) throws Exception {
		ByteArrayOutputStream os = new ByteArrayOutputStream();

		JAXBUtil.marshal(object, new StreamResult(os));

		byte[] buffer = os.toByteArray();

		ByteArrayInputStream is = new ByteArrayInputStream(buffer);

		return JAXBUtil.unmarshal(new StreamSource(is));
	}
}