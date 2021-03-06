/*
 * Copyright (c) 2017, Oracle and/or its affiliates. All rights reserved.
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
 *
 * This code is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 2 only, as
 * published by the Free Software Foundation.  Oracle designates this
 * particular file as subject to the "Classpath" exception as provided
 * by Oracle in the LICENSE file that accompanied this code.
 *
 * This code is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License
 * version 2 for more details (a copy is included in the LICENSE file that
 * accompanied this code).
 *
 * You should have received a copy of the GNU General Public License version
 * 2 along with this work; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110-1301 USA.
 *
 * Please contact Oracle, 500 Oracle Parkway, Redwood Shores, CA 94065 USA
 * or visit www.oracle.com if you need additional information or have any
 * questions.
 */
package com.oracle.truffle.tools.chromeinspector.instrument;

import com.oracle.truffle.api.instrumentation.EventBinding;
import com.oracle.truffle.api.instrumentation.SourceSectionFilter;
import com.oracle.truffle.api.instrumentation.TruffleInstrument;

import com.oracle.truffle.tools.chromeinspector.ScriptsHandler;

@TruffleInstrument.Registration(id = SourceLoadInstrument.ID, services = {Enabler.class, ScriptsHandler.Provider.class}, internal = true)
public final class SourceLoadInstrument extends TruffleInstrument implements Enabler, ScriptsHandler.Provider {

    public static final String ID = "SourceLoadInstrument";

    private Env env;
    private ScriptsHandler slh;
    private EventBinding<ScriptsHandler> binding;

    public SourceLoadInstrument() {
    }

    @Override
    protected void onCreate(Env e) {
        this.env = e;
        e.registerService(this);
    }

    @Override
    protected void onDispose(Env e) {
        disable();
        super.onDispose(e);
    }

    @Override
    public void enable() {
        if (slh == null) {
            slh = new ScriptsHandler();
            binding = env.getInstrumenter().attachLoadSourceListener(SourceSectionFilter.ANY, slh, true);
        }
    }

    @Override
    public void disable() {
        binding.dispose();
        slh = null;
    }

    @Override
    public ScriptsHandler getScriptsHandler() {
        return slh;
    }

}
