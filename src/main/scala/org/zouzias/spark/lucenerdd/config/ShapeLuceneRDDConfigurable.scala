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

import org.locationtech.spatial4j.io.ShapeIO

/**
 * Configuration for ShapeLuceneRDD's spatial functionality
 */
trait ShapeLuceneRDDConfigurable extends LuceneRDDConfigurable {

  /** Get the maximum level of the prefix tree */
  protected val getPrefixTreeMaxLevel: Int = params.prefixTreeMaxLevel

  /** Get the prefix tree name (geohash or quad) */
  protected val getPrefixTreeName: String = params.prefixTreeName

  /** Get the maximum distance error for the prefix tree */
  protected val getPrefixTreeMaxDistErr: Double = params.prefixTreeMaxDistErr

  /** Get the name of the location field */
  protected val getLocationFieldName: String = params.locationFieldName

  /** Get the shape format (GeoJSON, LEGACY, POLY, WKT) */
  protected val getShapeFormat: String = {
    val format = params.shapeFormat
    val availableFormats = Array(ShapeIO.GeoJSON, ShapeIO.LEGACY, ShapeIO.POLY, ShapeIO.WKT)
    if (availableFormats.contains(format)) format else ShapeIO.WKT
  }

  /** Get the method used for shape linkage */
  protected val getShapeLinkerMethod: String = params.shapeLinkerMethod
}
