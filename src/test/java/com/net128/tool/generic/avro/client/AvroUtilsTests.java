package com.net128.tool.generic.avro.client;

import org.apache.avro.Schema;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
import java.net.URISyntaxException;

import static org.assertj.core.api.Fail.fail;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
class AvroUtilsTests {
	@Autowired
	private AvroUtils avroUtils;

	@Autowired
	private SchemaRegistryService schemaRegistryService;

	@Test
	void testSerializeDeserialize1() throws Exception {
		schemaRegistryService.addSchema("user", "classpath:avro/user.avsc");
		var json = ResourceUtil.loadResource("User.json");
		byte [] avro = avroUtils.serializeToAvro("user", json);
		var resultJson = avroUtils.deserializeAvro("user", avro);
		assertEquals(json, resultJson);
	}
}
