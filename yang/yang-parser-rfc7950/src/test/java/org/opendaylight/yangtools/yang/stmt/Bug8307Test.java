/*
 * Copyright (c) 2017 Pantheon Technologies s.r.o. and others. All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */
package org.opendaylight.yangtools.yang.stmt;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.opendaylight.yangtools.yang.stmt.StmtTestUtils.sourceForResource;

import com.google.common.collect.ImmutableSetMultimap;
import com.google.common.collect.SetMultimap;
import java.net.URI;
import org.junit.Test;
import org.opendaylight.yangtools.yang.common.QName;
import org.opendaylight.yangtools.yang.common.QNameModule;
import org.opendaylight.yangtools.yang.common.Revision;
import org.opendaylight.yangtools.yang.model.api.SchemaContext;
import org.opendaylight.yangtools.yang.model.api.SchemaPath;
import org.opendaylight.yangtools.yang.model.util.SchemaContextUtil;
import org.opendaylight.yangtools.yang.parser.rfc7950.reactor.RFC7950Reactors;
import org.opendaylight.yangtools.yang.parser.spi.meta.InferenceException;
import org.opendaylight.yangtools.yang.parser.spi.meta.ReactorException;
import org.opendaylight.yangtools.yang.parser.spi.source.StatementStreamSource;
import org.opendaylight.yangtools.yang.parser.stmt.reactor.CrossSourceStatementReactor.BuildAction;

public class Bug8307Test {

    private static final StatementStreamSource FOO_MODULE = sourceForResource("/bugs/bug8307/foo.yang");
    private static final StatementStreamSource BAR_MODULE = sourceForResource("/bugs/bug8307/bar.yang");
    private static final StatementStreamSource BAZ_MODULE = sourceForResource("/bugs/bug8307/baz.yang");
    private static final StatementStreamSource FOOBAR_MODULE = sourceForResource("/bugs/bug8307/foobar.yang");
    private static final StatementStreamSource FOO_INVALID_MODULE = sourceForResource("/bugs/bug8307/foo-invalid.yang");
    private static final StatementStreamSource BAR_INVALID_MODULE = sourceForResource("/bugs/bug8307/bar-invalid.yang");
    private static final StatementStreamSource BAZ_INVALID_MODULE = sourceForResource("/bugs/bug8307/baz-invalid.yang");

    private static final URI FOO_NS = URI.create("foo-ns");
    private static final URI BAR_NS = URI.create("bar-ns");
    private static final URI BAZ_NS = URI.create("baz-ns");

    private static final Revision REVISION = Revision.of("2017-05-16");
    private static final QNameModule FOO = QNameModule.create(FOO_NS, REVISION);
    private static final QName MY_FOO_CONT_A = QName.create(FOO, "my-foo-cont-a");
    private static final QName MY_FOO_CONT_B = QName.create(FOO, "my-foo-cont-b");
    private static final QName MY_FOO_CONT_C = QName.create(FOO, "my-foo-cont-c");
    private static final QNameModule BAR = QNameModule.create(BAR_NS, REVISION);
    private static final QName MY_BAR_CONT_A = QName.create(BAR, "my-bar-cont-a");
    private static final QName MY_BAR_CONT_B = QName.create(BAR, "my-bar-cont-b");
    private static final QNameModule BAZ = QNameModule.create(BAZ_NS, REVISION);
    private static final QName MY_BAZ_CONT = QName.create(BAZ, "my-baz-cont");

    @Test
    public void testDeviationsSupportedInSomeModules() throws Exception {
        final SetMultimap<QNameModule, QNameModule> modulesWithSupportedDeviations =
                ImmutableSetMultimap.<QNameModule, QNameModule>builder()
                .put(FOO, BAR)
                .put(FOO, BAZ)
                .put(BAR, BAZ)
                .build();

        final SchemaContext schemaContext = RFC7950Reactors.defaultReactor().newBuild()
                .addSources(FOO_MODULE, BAR_MODULE, BAZ_MODULE, FOOBAR_MODULE)
                .setModulesWithSupportedDeviations(modulesWithSupportedDeviations)
                .buildEffective();
        assertNotNull(schemaContext);

        assertNull(SchemaContextUtil.findDataSchemaNode(schemaContext, SchemaPath.create(true, MY_FOO_CONT_A)));
        assertNull(SchemaContextUtil.findDataSchemaNode(schemaContext, SchemaPath.create(true, MY_FOO_CONT_B)));
        assertNotNull(SchemaContextUtil.findDataSchemaNode(schemaContext, SchemaPath.create(true, MY_FOO_CONT_C)));
        assertNull(SchemaContextUtil.findDataSchemaNode(schemaContext, SchemaPath.create(true, MY_BAR_CONT_A)));
        assertNotNull(SchemaContextUtil.findDataSchemaNode(schemaContext, SchemaPath.create(true, MY_BAR_CONT_B)));
    }

    @Test
    public void testDeviationsSupportedInAllModules() throws Exception {
        final SchemaContext schemaContext = RFC7950Reactors.defaultReactor().newBuild()
                .addSources(FOO_MODULE, BAR_MODULE, BAZ_MODULE, FOOBAR_MODULE)
                .buildEffective();
        assertNotNull(schemaContext);

        assertNull(SchemaContextUtil.findDataSchemaNode(schemaContext, SchemaPath.create(true, MY_FOO_CONT_A)));
        assertNull(SchemaContextUtil.findDataSchemaNode(schemaContext, SchemaPath.create(true, MY_FOO_CONT_B)));
        assertNull(SchemaContextUtil.findDataSchemaNode(schemaContext, SchemaPath.create(true, MY_FOO_CONT_C)));
        assertNull(SchemaContextUtil.findDataSchemaNode(schemaContext, SchemaPath.create(true, MY_BAR_CONT_A)));
        assertNull(SchemaContextUtil.findDataSchemaNode(schemaContext, SchemaPath.create(true, MY_BAR_CONT_B)));
    }

    @Test
    public void testDeviationsSupportedInNoModule() throws Exception {
        final SchemaContext schemaContext = RFC7950Reactors.defaultReactor().newBuild()
                .addSources(FOO_MODULE, BAR_MODULE, BAZ_MODULE, FOOBAR_MODULE)
                .setModulesWithSupportedDeviations(ImmutableSetMultimap.of())
                .buildEffective();
        assertNotNull(schemaContext);

        assertNotNull(SchemaContextUtil.findDataSchemaNode(schemaContext, SchemaPath.create(true, MY_FOO_CONT_A)));
        assertNotNull(SchemaContextUtil.findDataSchemaNode(schemaContext, SchemaPath.create(true, MY_FOO_CONT_B)));
        assertNotNull(SchemaContextUtil.findDataSchemaNode(schemaContext, SchemaPath.create(true, MY_FOO_CONT_C)));
        assertNotNull(SchemaContextUtil.findDataSchemaNode(schemaContext, SchemaPath.create(true, MY_BAR_CONT_A)));
        assertNotNull(SchemaContextUtil.findDataSchemaNode(schemaContext, SchemaPath.create(true, MY_BAR_CONT_B)));
    }

    @Test
    public void shouldFailOnAttemptToDeviateTheSameModule() {
        final BuildAction reactor = RFC7950Reactors.defaultReactor().newBuild().addSources(FOO_INVALID_MODULE);

        try {
            reactor.buildEffective();
            fail("Deviation that targets the same module as the one it is defined is forbidden.");
        } catch (final ReactorException ex) {
            final Throwable cause = ex.getCause();
            assertTrue(cause instanceof InferenceException);
            assertTrue(cause.getMessage().startsWith(
                    "Deviation must not target the same module as the one it is defined in"));
        }
    }

    @Test
    public void shouldFailOnAttemptToDeviateTheSameModule2() {
        final BuildAction reactor = RFC7950Reactors.defaultReactor().newBuild()
                .addSources(BAR_INVALID_MODULE, BAZ_INVALID_MODULE);

        try {
            reactor.buildEffective();
            fail("Deviation that targets the same module as the one it is defined is forbidden.");
        } catch (final ReactorException ex) {
            final Throwable cause = ex.getCause();
            assertTrue(cause instanceof InferenceException);
            assertTrue(cause.getMessage().startsWith(
                    "Deviation must not target the same module as the one it is defined in"));
        }
    }
}
