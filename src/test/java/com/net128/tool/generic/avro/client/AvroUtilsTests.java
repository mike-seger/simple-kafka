package com.net128.tool.generic.avro.client;

import com.net128.tool.generic.avro.client.util.AvroUtil;
import com.net128.tool.generic.avro.client.util.ResourceUtil;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@SuppressWarnings("unused")
class AvroUtilsTests {
	@Autowired
	private AvroUtil avroUtil;

	@Autowired
	private SchemaRegistryService schemaRegistryService;

	@Test
	void testSerializeDeserialize1() throws Exception {
		schemaRegistryService.addSchema("user1", "classpath:avro/user1.avsc");
		var json = ResourceUtil.loadResourceFromLocation("User.json");
		byte [] avro = avroUtil.serializeToAvro("user1", json);
		var resultJson = avroUtil.deserializeAvro("user1", avro);
		assertEquals(json, resultJson);
	}
}
