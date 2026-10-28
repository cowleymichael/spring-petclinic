/*
 * Copyright 2012-2026 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.springframework.samples.petclinic.owner;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.not;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Web-layer tests for the support-diagnostics switch (2026-10-28 midstream scope change):
 * with {@code petclinic.privacy.mask-telephones=false} the owners list page shows full
 * telephone numbers. Runs with the full application context on the seeded in-memory H2
 * database so the constructor-injected flag is honored exactly as in production.
 */
@SpringBootTest(properties = "petclinic.privacy.mask-telephones=false")
@AutoConfigureMockMvc
class OwnerTelephoneDiagnosticsTests {

	@Autowired
	private MockMvc mockMvc;

	@Test
	void ownersListShowsFullTelephoneNumberInDiagnosticsMode() throws Exception {
		mockMvc.perform(get("/owners?page=1"))
			.andExpect(status().isOk())
			.andExpect(content().string(containsString("6085551023")))
			.andExpect(content().string(not(containsString("******1023"))));
	}

	@Test
	void ownerDetailStillShowsFullTelephoneNumber() throws Exception {
		mockMvc.perform(get("/owners/1"))
			.andExpect(status().isOk())
			.andExpect(content().string(containsString("6085551023")));
	}

}
