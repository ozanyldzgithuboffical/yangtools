/*
 * Copyright (c) 2020 PANTHEON.tech, s.r.o. and others.  All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */
package org.opendaylight.yangtools.yang.parser.rfc7950.stmt.must;

import org.opendaylight.yangtools.yang.model.api.RevisionAwareXPath;
import org.opendaylight.yangtools.yang.model.api.stmt.MustStatement;
import org.opendaylight.yangtools.yang.parser.rfc7950.stmt.AbstractDeclaredEffectiveStatement.DefaultArgument;

final class EmptyMustEffectiveStatement extends DefaultArgument<RevisionAwareXPath, MustStatement>
        implements MustDefinitionMixin {
    EmptyMustEffectiveStatement(final MustStatement declared) {
        super(declared);
    }

    // FIXME: 6.0.0: remove this method
    @Override
    public String toString() {
        return argument().getOriginalString();
    }
}
