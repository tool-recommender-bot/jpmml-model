/*
 * Copyright (c) 2014 Villu Ruusmann
 */
package org.jpmml.model;

import org.dmg.pmml.Version;
import org.junit.Test;
import org.w3c.dom.Node;

import static org.junit.Assert.assertEquals;

public class ApplyTest {

	@Test
	public void transform() throws Exception {
		byte[] original = ResourceUtil.getByteArray(ApplyTest.class);

		checkApply(original, "", null);

		byte[] latest = VersionUtil.upgradeToLatest(original);

		checkApply(latest, null, "");

		byte[] latestToOriginal = VersionUtil.downgrade(latest, Version.PMML_4_1);

		checkApply(latestToOriginal, "", null);
	}

	static
	private void checkApply(byte[] bytes, String mapMissingTo, String defaultValue) throws Exception {
		Node node = XPathUtil.selectNode(bytes, "/:PMML/:TransformationDictionary/:DerivedField/:Apply");

		assertEquals(mapMissingTo, DOMUtil.getAttributeValue(node, "mapMissingTo"));
		assertEquals(defaultValue, DOMUtil.getAttributeValue(node, "defaultValue"));
	}
}