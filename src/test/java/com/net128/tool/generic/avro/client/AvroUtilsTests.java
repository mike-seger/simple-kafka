package com.net128.tool.generic.avro.client;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@SuppressWarnings("unused")
class AvroUtilsTests {
	@Autowired
	private AvroUtils avroUtils;

	@Autowired
	private SchemaRegistryService schemaRegistryService;

	@Test
	void testSerializeDeserialize1() throws Exception {
		schemaRegistryService.addSchema("user1", "classpath:avro/user1.avsc");
		var json = ResourceUtil.loadResourceFromLocation("User.json");
		byte [] avro = avroUtils.serializeToAvro("user1", json);
		var resultJson = avroUtils.deserializeAvro("user1", avro);
		assertEquals(json, resultJson);
	}
}
