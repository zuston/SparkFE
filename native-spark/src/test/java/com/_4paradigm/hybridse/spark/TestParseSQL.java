/*
 * Copyright 2021 4Paradigm
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com._4paradigm.hybridse.spark;

import com._4paradigm.hybridse.LibraryLoader;
import com._4paradigm.hybridse.base.BaseStatus;
import com._4paradigm.hybridse.type.TypeOuterClass;
import com._4paradigm.hybridse.type.TypeOuterClass.TableDef;
import com._4paradigm.hybridse.type.TypeOuterClass.ColumnDef;
import com._4paradigm.hybridse.type.TypeOuterClass.Database;
import com._4paradigm.hybridse.vm.*;
import org.junit.Test;

import java.nio.ByteBuffer;

import static org.junit.Assert.assertTrue;


public class TestParseSQL {

    // TODO: Add this test case if needed
    /* @Test
    public void testParseSQL() {

        LibraryLoader.loadLibrary("libhybridse_jsdk_core");
        ColumnDef col1 = ColumnDef.newBuilder()
                .setName("col_1").setType(TypeOuterClass.Type.kDouble).build();
        ColumnDef col2 = ColumnDef.newBuilder()
                .setName("col_2").setType(TypeOuterClass.Type.kInt32).build();
        TableDef table = TableDef.newBuilder()
                .setName("t1")
                .addColumns(col1)
                .addColumns(col2)
                .build();
        Database db = Database.newBuilder()
                .setName("db")
                .addTables(table).build();

        Engine.InitializeGlobalLLVM();

        SimpleCatalog catalog = new SimpleCatalog();
        catalog.AddDatabase(db);

        BatchRunSession sess = new BatchRunSession();

        EngineOptions options = new EngineOptions();
        options.set_keep_ir(true);

        BaseStatus status = new BaseStatus();
        Engine engine = new Engine(catalog, options);
        assertTrue(engine.Get("select col_1, col_2 from t1;", "db", sess, status));
        CompileInfo compileInfo = sess.GetCompileInfo();

        long size = compileInfo.GetIRSize();
        ByteBuffer buffer = ByteBuffer.allocateDirect(Long.valueOf(size).intValue());
        compileInfo.GetIRBuffer(buffer);
        System.err.println("Dumped module string: len=" + size);

        PhysicalOpNode root = compileInfo.GetPhysicalPlan();
        root.Print();

        HybridSEJITWrapper jit = HybridSEJITWrapper.Create();
        jit.Init();
        HybridSEJITWrapper.InitJITSymbols(jit);
        jit.AddModuleFromBuffer(buffer);

        engine.delete();
        jit.delete();
        options.delete();
        status.delete();
        sess.delete();
        catalog.delete();
    }
     */

}