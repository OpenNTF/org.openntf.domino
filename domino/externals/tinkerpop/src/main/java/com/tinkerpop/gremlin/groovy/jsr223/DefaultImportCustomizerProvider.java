package com.tinkerpop.gremlin.groovy.jsr223;

import java.util.HashSet;
import java.util.Set;

/**
 * Grabs the standard Gremlin/Blueprints classes and allows additional imports to be added.
 *
 * @author Stephen Mallette (http://stephen.genoprime.com)
 */
public class DefaultImportCustomizerProvider extends AbstractImportCustomizerProvider {
    private static Set<String> staticExtraImports = new HashSet<String>();
    private static Set<String> staticExtraStaticImports = new HashSet<String>();

    /**
     * Utilizes imports defined statically by initializeStatically().
     */
    public DefaultImportCustomizerProvider() {
        this.extraImports.addAll(staticExtraImports);
        this.extraStaticImports.addAll(staticExtraStaticImports);
    }

    /**
     * Utilizes imports defined by the supplied arguments.  Those imports defined statically through
     * initializeStatically() are ignored.
     */
    public DefaultImportCustomizerProvider(final Set<String> extraImports, final Set<String> extraStaticImports) {
        this.extraStaticImports.addAll(extraStaticImports);
        this.extraImports.addAll(extraImports);
    }

    /**
     * Allows imports to defined globally and statically.
     * 
     * This method must be called prior to initialization of a ScriptEngine instance through the ScriptEngineFactory.
     */
    public static void initializeStatically(final Set<String> extraImports, final Set<String> extraStaticImports) {
        if (extraImports != null) {
            staticExtraImports = extraImports;
        }

        if (extraStaticImports != null) {
            staticExtraStaticImports = extraStaticImports;
        }
    }
}
