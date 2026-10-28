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

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Unit tests for {@link TelephoneMasker}: the masking rule must be deterministic - reveal
 * only the last 4 characters and mask every character before them with one asterisk each.
 * Also covers the support-diagnostics switch: with masking disabled, values are returned
 * unchanged.
 */
class TelephoneMaskerTests {

	private final TelephoneMasker telephoneMasker = new TelephoneMasker(true);

	@Test
	void masksTenDigitTelephoneKeepingLastFourDigits() {
		assertThat(telephoneMasker.mask("6085551023")).isEqualTo("******1023");
	}

	@Test
	void masksExactlyFourCharactersEntirely() {
		assertThat(telephoneMasker.mask("1234")).isEqualTo("****");
	}

	@Test
	void masksValuesShorterThanFourCharactersEntirely() {
		assertThat(telephoneMasker.mask("12")).isEqualTo("**");
	}

	@Test
	void passesEmptyStringThroughUnchanged() {
		assertThat(telephoneMasker.mask("")).isEmpty();
	}

	@Test
	void passesNullThroughUnchanged() {
		assertThat(telephoneMasker.mask(null)).isNull();
	}

	@Test
	void masksByCharacterPositionEvenForFormattedInput() {
		// 14 characters: 10 asterisks, then the last 4 characters.
		assertThat(telephoneMasker.mask("(608) 555-1023")).isEqualTo("**********1023");
	}

	@Test
	void diagnosticsModeReturnsTenDigitTelephoneUnchanged() {
		assertThat(new TelephoneMasker(false).mask("6085551023")).isEqualTo("6085551023");
	}

	@Test
	void diagnosticsModeReturnsFormattedInputUnchanged() {
		assertThat(new TelephoneMasker(false).mask("(608) 555-1023")).isEqualTo("(608) 555-1023");
	}

	@Test
	void diagnosticsModeStillPassesNullThrough() {
		assertThat(new TelephoneMasker(false).mask(null)).isNull();
	}

	@Test
	void diagnosticsModeStillPassesEmptyStringThrough() {
		assertThat(new TelephoneMasker(false).mask("")).isEmpty();
	}

}
