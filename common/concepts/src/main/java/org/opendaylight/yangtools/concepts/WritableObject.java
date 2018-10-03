/*
 * Copyright (c) 2016 Cisco Systems, Inc. and others.  All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */
package org.opendaylight.yangtools.concepts;

import com.google.common.annotations.Beta;
import java.io.DataOutput;
import java.io.IOException;
import org.eclipse.jdt.annotation.NonNull;

/**
 * Marker interface for an object which can be written out to an {@link DataOutput}. Classes implementing this
 * interface should declare a corresponding
 *
 * <pre>
 *      public static CLASS readFrom(DataInput in) throws IOException;
 * </pre>
 *
 * <p>The serialization format provided by this abstraction does not guarantee versioning. Callers are responsible
 * for ensuring the source stream is correctly positioned.
 *
 * @author Robert Varga
 */
@Beta
public interface WritableObject {
    /**
     * Serialize this object into a {@link DataOutput} as a fixed-format stream.
     *
     * @param out Data output
     * @throws IOException if an I/O error occurs
     * @throws NullPointerException if out is null
     */
    void writeTo(@NonNull DataOutput out) throws IOException;
}
