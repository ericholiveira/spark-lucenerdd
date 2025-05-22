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
package org.zouzias.spark.lucenerdd.query

import org.apache.lucene.search.similarities.{ClassicSimilarity, Similarity}
import org.zouzias.spark.lucenerdd.config.Configurable
import org.apache.spark.internal.Logging

/**
 * Lucene Similarity loader via configuration
 */
trait SimilarityConfigurable extends Configurable with Logging {

  protected val LuceneSimilarityConfigValue: Option[String] = Some(params.similarityName)

  protected def getOrElseClassic(): String = LuceneSimilarityConfigValue.getOrElse("classic")

  protected def getSimilarity(similarityName: Option[String]): Similarity = {
    if (similarityName.isDefined) {
      params.getSimilarityByName(similarityName.get)
    }
    else {
      logInfo("Similarity name is not defined. Using ClassicSimilarity.")
      new ClassicSimilarity()
    }
  }
}
