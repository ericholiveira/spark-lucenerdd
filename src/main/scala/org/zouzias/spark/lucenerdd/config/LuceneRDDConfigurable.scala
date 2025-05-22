/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.zouzias.spark.lucenerdd.config

import org.apache.lucene.index.IndexOptions

/**
 * Configuration for [[org.zouzias.spark.lucenerdd.LuceneRDD]]
 */
trait LuceneRDDConfigurable extends Configurable {

  /** Maximum value for topK queries */
  protected val MaxDefaultTopKValue: Int = params.maxTopKValue

  /** Default value for topK queries */
  protected val DefaultTopK: Int = params.defaultTopK

  /** Default value for number of faceted results */
  protected val DefaultFacetNum: Int = params.defaultFacetNum

  /** Whether to analyze string fields by default */
  protected val StringFieldsDefaultAnalyzed: Boolean = params.stringFieldsAnalyzed

  /** List of string fields that should not be analyzed */
  protected val StringFieldsListToBeNotAnalyzed: List[String] = params.nonAnalyzedFields

  /** Whether to store term vectors */
  protected val StringFieldsStoreTermVector: Boolean = params.storeTermVectors

  /** Whether to store term positions */
  protected val StringFieldsStoreTermPositions: Boolean = params.storeTermPositions

  /** Whether to omit norms */
  protected val StringFieldsOmitNorms: Boolean = params.omitNorms

  /** Index options configuration */
  protected val StringFieldsIndexOptions: IndexOptions = params.indexOptions

  /** Method used for record linkage */
  protected val getLinkerMethod: String = params.linkerMethod

  /** Name of global analyzer used for indexing */
  protected val getIndexAnalyzerName: String = params.indexAnalyzerName

  /** Name of global analyzer used for querying */
  protected val getQueryAnalyzerName: String = params.queryAnalyzerName

  /** Name of Lucene similarity implementation */
  protected val getSimilarityName: String = params.similarityName

  /** Map of field-specific analyzer names for indexing */
  protected val getIndexAnalyzerPerFieldNames: Map[String, String] =
    params.indexAnalyzerPerFieldNames

  /** Map of field-specific analyzer names for querying */
  protected val getQueryAnalyzerPerFieldNames: Map[String, String] =
    params.queryAnalyzerPerFieldNames
}
