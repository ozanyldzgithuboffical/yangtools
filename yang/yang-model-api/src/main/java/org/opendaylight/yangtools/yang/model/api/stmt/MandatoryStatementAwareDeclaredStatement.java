/*
 * Copyright (c) 2018 Pantheon Technologies, s.r.o. and others.  All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */
package org.opendaylight.yangtools.yang.model.api.stmt;

import com.google.common.annotations.Beta;
import java.util.Optional;
import org.eclipse.jdt.annotation.NonNull;
import org.opendaylight.yangtools.yang.model.api.meta.DeclaredStatement;

/**
 * Marker interface for statements which may contain a 'mandatory' statement, as defined in RFC7950.
 */
@Beta
public interface MandatoryStatementAwareDeclaredStatement<T> extends DeclaredStatement<T> {
    /**
     * Return a {@link MandatoryStatement} child, if present.
     *
     * @return A {@link MandatoryStatement}, or empty if none is present.
     */
    default @NonNull Optional<MandatoryStatement> getMandatory() {
        return findFirstDeclaredSubstatement(MandatoryStatement.class);
    }
}
