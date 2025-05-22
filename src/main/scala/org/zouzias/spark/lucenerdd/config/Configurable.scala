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

import com.typesafe.config.ConfigFactory
import org.apache.lucene.index.IndexOptions
import org.locationtech.spatial4j.io.ShapeIO
import scala.collection.JavaConverters._

/**
 * Load and extract configuration parameters
 */
trait Configurable extends Serializable {
  protected val params: LuceneRDDParams = {
    val config = ConfigFactory.load()

    // Core parameters
    val maxTopK = if (config.hasPath("lucenerdd.query.topk.maxvalue")) {
      config.getInt("lucenerdd.query.topk.maxvalue")
    } else 1000

    val defaultTopK = if (config.hasPath("lucenerdd.query.topk.default")) {
      config.getInt("lucenerdd.query.topk.default")
    } else 10

    val defaultFacetNum = if (config.hasPath("lucenerdd.query.facets.number.default")) {
      config.getInt("lucenerdd.query.facets.number.default")
    } else 10

    val stringFieldsAnalyzed = if (config.hasPath("lucenerdd.index.stringfields.analyzed")) {
      config.getBoolean("lucenerdd.index.stringfields.analyzed")
    } else true

    val nonAnalyzedFields = if (config.hasPath("lucenerdd.index.stringfields.not_analyzed_list")) {
      config.getStringList("lucenerdd.index.stringfields.not_analyzed_list").asScala.toList
    } else List.empty[String]

    val storeTermVectors = if (config.hasPath("lucenerdd.index.stringfields.terms.vectors")) {
      config.getBoolean("lucenerdd.index.stringfields.terms.vectors")
    } else true

    val storeTermPositions = if (config.hasPath("lucenerdd.index.stringfields.terms.positions")) {
      config.getBoolean("lucenerdd.index.stringfields.terms.positions")
    } else true

    val omitNorms = if (config.hasPath("lucenerdd.index.stringfields.terms.omitnorms")) {
      config.getBoolean("lucenerdd.index.stringfields.terms.omitnorms")
    } else false

    val indexOptions = if (config.hasPath("lucenerdd.index.stringfields.options")) {
      val indexOptionsStr = config.getString("lucenerdd.index.stringfields.options").toLowerCase
      indexOptionsStr match {
        case "docs" => IndexOptions.DOCS
        case "docs_and_freqs" => IndexOptions.DOCS_AND_FREQS
        case "docs_and_freqs_and_positions" => IndexOptions.DOCS_AND_FREQS_AND_POSITIONS
        case "docs_and_freqs_and_positions_and_offsets" =>
          IndexOptions.DOCS_AND_FREQS_AND_POSITIONS_AND_OFFSETS
        case _ => IndexOptions.DOCS_AND_FREQS_AND_POSITIONS
      }
    } else IndexOptions.DOCS_AND_FREQS_AND_POSITIONS

    val linkerMethod = if (config.hasPath("lucenerdd.linker.method")) {
      config.getString("lucenerdd.linker.method")
    } else "collectbroadcast"

    // Analyzer configurations
    val indexAnalyzerName = if (config.hasPath("lucenerdd.analyzer.name")) {
      config.getString("lucenerdd.analyzer.name")
    } else "standard"

    val queryAnalyzerName = if (config.hasPath("lucenerdd.query.analyzer.name")) {
      config.getString("lucenerdd.query.analyzer.name")
    } else indexAnalyzerName

    // Similarity configuration
    val similarityName = if (config.hasPath("lucenerdd.similarity.name")) {
      config.getString("lucenerdd.similarity.name")
    } else "classic"

    // Shape configurations
    val prefixTreeMaxLevel = if (config.hasPath("lucenerdd.spatial.prefixtree.maxlevel")) {
      config.getInt("lucenerdd.spatial.prefixtree.maxlevel")
    } else 11

    val prefixTreeName = if (config.hasPath("lucenerdd.spatial.prefixtree.name")) {
      config.getString("lucenerdd.spatial.prefixtree.name")
    } else "geohash"

    val prefixTreeMaxDistErr = if (config.hasPath("lucenerdd.spatial.prefixtree.maxDistErr")) {
      config.getDouble("lucenerdd.spatial.prefixtree.maxDistErr")
    } else 1.0

    val locationFieldName = if (config.hasPath("lucenerdd.spatial.location.field.name")) {
      config.getString("lucenerdd.spatial.location.field.name")
    } else "__location__"

    val shapeFormat = if (config.hasPath("lucenerdd.spatial.shape.io.format")) {
      val format = config.getString("lucenerdd.spatial.shape.io.format")
      val availableFormats = Array(ShapeIO.GeoJSON, ShapeIO.LEGACY, ShapeIO.POLY, ShapeIO.WKT)
      if (availableFormats.contains(format)) format else ShapeIO.WKT
    } else ShapeIO.WKT

    val shapeLinkerMethod = if (config.hasPath("lucenerdd.spatial.linker.method")) {
      config.getString("lucenerdd.spatial.linker.method")
    } else "collectbroadcast"

    // Storage configuration
    val indexStoreMode = if (config.hasPath("lucenerdd.index.store.mode")) {
      config.getString("lucenerdd.index.store.mode")
    } else "disk"

    LuceneRDDParams(
      maxTopKValue = maxTopK,
      defaultTopK = defaultTopK,
      defaultFacetNum = defaultFacetNum,
      stringFieldsAnalyzed = stringFieldsAnalyzed,
      nonAnalyzedFields = nonAnalyzedFields,
      storeTermVectors = storeTermVectors,
      storeTermPositions = storeTermPositions,
      omitNorms = omitNorms,
      indexOptions = indexOptions,
      linkerMethod = linkerMethod,
      indexAnalyzerName = indexAnalyzerName,
      queryAnalyzerName = queryAnalyzerName,
      similarityName = similarityName,
      prefixTreeMaxLevel = prefixTreeMaxLevel,
      prefixTreeName = prefixTreeName,
      prefixTreeMaxDistErr = prefixTreeMaxDistErr,
      locationFieldName = locationFieldName,
      shapeFormat = shapeFormat,
      shapeLinkerMethod = shapeLinkerMethod,
      indexStoreMode = indexStoreMode
    )
  }
}
