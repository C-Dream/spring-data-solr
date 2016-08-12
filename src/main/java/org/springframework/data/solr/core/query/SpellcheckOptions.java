/*
 * Copyright 2016 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.springframework.data.solr.core.query;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

import org.apache.solr.common.params.SpellingParams;
import org.springframework.util.StringUtils;

/**
 * {@link SpellcheckOptions} allows modification of query parameters targeting the SpellCheck component is designed to
 * provide inline query suggestions based on other, similar, terms.
 * 
 * @author Christoph Strobl
 * @since 2.1
 */
public class SpellcheckOptions {

	private static final String QT = "/spell";

	private String qt;
	private Query query;
	private Map<String, Object> params;

	private SpellcheckOptions(Query query, String qt, Map<String, Object> params) {

		this.query = query;
		this.params = new LinkedHashMap<String, Object>(params);
	}

	/**
	 * Creates new {@link SpellcheckOptions}.
	 * 
	 * @return
	 */
	public static SpellcheckOptions spellcheck() {
		return new SpellcheckOptions(null, QT, new LinkedHashMap<String, Object>());
	}

	/**
	 * Creates new {@link SpellcheckOptions} with a given {@link Query}.
	 * 
	 * @param q
	 * @return
	 */
	public static SpellcheckOptions spellcheck(Query q) {
		return new SpellcheckOptions(q, QT, new LinkedHashMap<String, Object>());
	}

	/**
	 * Get the query to be used for spellchecking.
	 * 
	 * @return can be {@literal null}.
	 */
	public Query getQuery() {
		return query;
	}

	public String getQt() {
		return StringUtils.hasText(qt) ? qt : QT;
	}

	/**
	 * @return never {@literal null}.
	 */
	public Map<String, Object> getParams() {
		return Collections.unmodifiableMap(params);
	}

	/**
	 * If set, Solr creates the dictionary that the SolrSpellChecker will use for spell-checking
	 * 
	 * @return new {@link SpellcheckOptions}
	 */
	public SpellcheckOptions buildDictionary() {
		return createNewAndAppend(SpellingParams.SPELLCHECK_BUILD, true);
	}

	/**
	 * If set, Solr will take the best suggestion for each token (if one exists) and construct a new query from the
	 * suggestions.
	 * 
	 * @return new {@link SpellcheckOptions}
	 */
	public SpellcheckOptions collate() {
		return createNewAndAppend(SpellingParams.SPELLCHECK_COLLATE, true);
	}

	/**
	 * The maximum number of collations to return.
	 * 
	 * @param max
	 * @return
	 */
	public SpellcheckOptions maxCollations(long max) {
		return potentiallySetCollate().createNewAndAppend(SpellingParams.SPELLCHECK_MAX_COLLATIONS, max);
	}

	/**
	 * This parameter specifies the number of collation possibilities for Solr to try before giving up.
	 * 
	 * @param tries
	 * @return
	 */
	public SpellcheckOptions maxCollationTries(long tries) {
		return potentiallySetCollate().createNewAndAppend(SpellingParams.SPELLCHECK_MAX_COLLATION_TRIES, tries);
	}

	/**
	 * This parameter specifies the maximum number of word correction combinations to rank and evaluate prior to deciding
	 * which collation candidates to test against the index.
	 * 
	 * @param evaluations
	 * @return
	 */
	public SpellcheckOptions maxCollationEvaluations(long evaluations) {
		return potentiallySetCollate().createNewAndAppend(SpellingParams.SPELLCHECK_MAX_COLLATION_EVALUATIONS, evaluations);
	}

	/**
	 * Instructs Solr to return an expanded response format detailing the collations found.
	 * 
	 * @return
	 */
	public SpellcheckOptions collateExtendedResults() {
		return potentiallySetCollate().createNewAndAppend(SpellingParams.SPELLCHECK_COLLATE_EXTENDED_RESULTS, true);
	}

	/**
	 * This parameter specifies the maximum number of documents that should be collect when testing potential collations
	 * against the index.
	 * 
	 * @param nr
	 * @return
	 */
	public SpellcheckOptions collateMaxCollectDocs(long nr) {
		return potentiallySetCollate().createNewAndAppend(SpellingParams.SPELLCHECK_COLLATE_MAX_COLLECT_DOCS, nr);
	}

	/**
	 * This parameter prefix can be used to specify any additional parameters that you wish to the Spellchecker to use
	 * when internally validating collation queries.
	 * 
	 * @param param
	 * @param value
	 * @return
	 */
	public SpellcheckOptions collateParam(String param, Object value) {
		return potentiallySetCollate().createNewAndAppend(SpellingParams.SPELLCHECK_COLLATE_PARAM_OVERRIDE + param, value);
	}

	/**
	 * Specifies the maximum number of spelling suggestions to be returned.
	 * 
	 * @param nr
	 * @return
	 */
	public SpellcheckOptions count(long nr) {
		return createNewAndAppend(SpellingParams.SPELLCHECK_COUNT, nr);
	}

	/**
	 * This parameter causes Solr to use the dictionary named in the parameter's argument. The default setting is
	 * "default". This parameter can be used to invoke a specific spellchecker on a per request basis.
	 * 
	 * @param name
	 * @return
	 */
	public SpellcheckOptions dictionary(String name) {
		return createNewAndAppend(SpellingParams.SPELLCHECK_DICT, name);
	}

	/**
	 * Limits spellcheck responses to queries that are more popular than the original query.
	 * 
	 * @return
	 */
	public SpellcheckOptions onlyMorePopular() {
		return createNewAndAppend(SpellingParams.SPELLCHECK_ONLY_MORE_POPULAR, true);
	}

	/**
	 * The maximum number of hits the request can return in order to both generate spelling suggestions and set the
	 * {@literal correctlySpelled} element to {@literal false}.
	 * 
	 * @param nr
	 * @return
	 */
	public SpellcheckOptions maxResultsForSuggest(long nr) {
		return createNewAndAppend(SpellingParams.SPELLCHECK_MAX_RESULTS_FOR_SUGGEST, nr);
	}

	/**
	 * The count of suggestions to return for each query term existing in the index and/or dictionary.
	 * 
	 * @param nr
	 * @return
	 */
	public SpellcheckOptions alternativeTermCount(long nr) {
		return createNewAndAppend(SpellingParams.SPELLCHECK_ALTERNATIVE_TERM_COUNT, nr);
	}

	/**
	 * Specifies an accuracy value to be used by the spell checking implementation to decide whether a result is
	 * worthwhile or not. The value is a float between 0 and 1.
	 * 
	 * @param nr
	 * @return
	 */
	public SpellcheckOptions accuracy(float nr) {
		return createNewAndAppend(SpellingParams.SPELLCHECK_ACCURACY, nr);
	}

	/**
	 * Set the query endpoint
	 * 
	 * @param qt
	 * @return
	 */
	public SpellcheckOptions qt(String qt) {
		return new SpellcheckOptions(query, qt, params);
	}

	private SpellcheckOptions potentiallySetCollate() {

		if (params.containsKey(SpellingParams.SPELLCHECK_COLLATE)) {
			return this;
		}

		return collate();
	}

	private SpellcheckOptions createNewAndAppend(String key, Object value) {

		SpellcheckOptions so = new SpellcheckOptions(query, qt, params);
		so.params.put(key, value);
		return so;
	}

}
