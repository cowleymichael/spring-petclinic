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

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * Masks owner telephone numbers for display on the owners list page.
 *
 * <p>
 * Display-only utility: it is applied exclusively at the single list render site in
 * {@code templates/owners/ownersList.html} and never alters stored values. The owner
 * detail page and the owner create/edit form intentionally keep the full number. The
 * masking rule reveals only the last 4 characters and replaces every character before
 * them with exactly one asterisk ({@code '*'}).
 *
 * <p>
 * Support-diagnostics switch: masking can be turned off with the
 * {@code petclinic.privacy.mask-telephones=false} property so Operations can see full
 * numbers on the owners list page; the default ({@code true}) keeps the list masked. The
 * owner detail page always shows the full number regardless of this switch.
 *
 * <p>
 * Registered as a Spring bean because Thymeleaf 3.1+ forbids SpEL type references
 * ({@code T(...)}) in template expressions, so the list template calls it as
 * {@code @telephoneMasker}.
 */
@Component
public final class TelephoneMasker {

	private final boolean maskTelephones;

	/**
	 * @param maskTelephones whether list-page telephone masking is enabled (property
	 * {@code petclinic.privacy.mask-telephones}, default {@code true})
	 */
	public TelephoneMasker(@Value("${petclinic.privacy.mask-telephones:true}") boolean maskTelephones) {
		this.maskTelephones = maskTelephones;
	}

	/**
	 * Mask the given telephone for list display, or return it unchanged when masking is
	 * disabled (support-diagnostics mode). The rule is character-position based and
	 * deterministic: {@code null} and the empty string are returned unchanged (the list
	 * page renders them as blank); values of length 4 or less are fully masked; longer
	 * values keep only their last 4 characters, e.g. {@code "6085551023"} becomes
	 * {@code "******1023"}.
	 * @param telephone the telephone value as stored (may be {@code null})
	 * @return the masked value for display, the unchanged input in diagnostics mode, or
	 * the unchanged {@code null}/empty input
	 */
	public String mask(String telephone) {
		if (!this.maskTelephones || telephone == null || telephone.isEmpty()) {
			return telephone;
		}
		int length = telephone.length();
		if (length <= 4) {
			return "*".repeat(length);
		}
		return "*".repeat(length - 4) + telephone.substring(length - 4);
	}

}
