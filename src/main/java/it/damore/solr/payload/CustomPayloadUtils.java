/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package it.damore.solr.payload;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.tokenattributes.TermToBytesRefAttribute;
import org.apache.lucene.index.Term;
import org.apache.lucene.queries.spans.SpanNearQuery;
import org.apache.lucene.queries.spans.SpanOrQuery;
import org.apache.lucene.queries.spans.SpanQuery;
import org.apache.lucene.queries.spans.SpanTermQuery;

public class CustomPayloadUtils {
  /**
   * The generated SpanQuery will be either a SpanTermQuery or an ordered, zero slop SpanNearQuery, depending
   * on how many tokens are emitted.
   */
  public static SpanQuery createSpanQuery(String field, String value, Analyzer analyzer, String operator, int slop, boolean inOrder) throws IOException {
    // adapted this from QueryBuilder.createSpanQuery (which isn't currently public) and added reset(), end(), and close() calls
    List<SpanTermQuery> terms = new ArrayList<>();
    try (TokenStream in = analyzer.tokenStream(field, value)) {
      in.reset();

      TermToBytesRefAttribute termAtt = in.getAttribute(TermToBytesRefAttribute.class);
      while (in.incrementToken()) {
        terms.add(new SpanTermQuery(new Term(field, termAtt.getBytesRef())));
      }
      in.end();
    }

    SpanQuery query;
    if (terms.isEmpty()) {
      query = null;
    } else if (terms.size() == 1) {
      query = terms.get(0);
    } else if (operator != null && operator.equalsIgnoreCase("or")) {
        query = new SpanOrQuery(terms.toArray(new SpanTermQuery[terms.size()]));
    } else {
        query = new SpanNearQuery(terms.toArray(new SpanTermQuery[terms.size()]), slop, inOrder);
      }
    return query;
  }
}
