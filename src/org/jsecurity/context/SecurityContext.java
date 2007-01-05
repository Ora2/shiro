/*
 * Copyright (C) 2005-2007 Les Hazlewood, Jeremy Haile
 *
 * This library is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as published
 * by the Free Software Foundation; either version 2.1 of the License, or
 * (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY
 * or FITNESS FOR A PARTICULAR PURPOSE. See the GNU Lesser General
 * Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this library; if not, write to the
 *
 * Free Software Foundation, Inc.
 * 59 Temple Place, Suite 330
 * Boston, MA 02111-1307
 * USA
 *
 * Or, you may view it online at
 * http://www.opensource.org/licenses/lgpl-license.php
 */
package org.jsecurity.context;

import org.jsecurity.authz.AuthorizationException;
import org.jsecurity.authz.NoSuchPrincipalException;
import org.jsecurity.session.Session;

import java.security.Permission;
import java.security.Principal;
import java.util.Collection;
import java.util.List;

/**
 * Provides all access control behavior and session access for a subject (aka a 'user' account).
 *
 * @see org.jsecurity.authc.Authenticator
 *
 * @since 0.1
 * @author Les Hazlewood
 * @author Jeremy Haile
 */
public interface SecurityContext {

    /**
     * Returns the primary identifier of the subject associated with this
     * <tt>SecurityContext</tt> (usually a user id or username).  This is a
     * convenience method for contexts that only use a single principal.  If multiple
     * principals are associated with the context, the primary principal will be returned.
     * The interpretation of the meaning of &quot;primary principal&quot; is left to the implementation
     * (although most will choose a unique identifier such as a user id or username).
     * @return the primary principal (a.k.a. identifying attribute) of the subject associated with this SecurityContext.
     * @throws org.jsecurity.authz.NoSuchPrincipalException is thrown if no principals are associated with this
     * authorization context.
     */
    Principal getPrincipal() throws NoSuchPrincipalException;

    /**
     * Returns all principals associated with this <tt>SecurityContext</tt>.
     * @return a collection of principals associated with this context, or an empty collection
     * if no principals are associated with this authorization context
     */
    List<Principal> getAllPrincipals();

    /**
     * Returns a single principal assignable from the specified type
     * that is associated with this <tt>SecurityContext</tt>.  If multiple principals of
     * this type are associated with this context, it is up to the specific implementation as
     * to which principal will be returned and may be undefined.
     * @param principalType the principal type that should be returned.
     * @return a principal of the specified type.
     * @throws NoSuchPrincipalException if no principals of this type are associated with this
     * context.
     */
    Principal getPrincipalByType( Class principalType ) throws NoSuchPrincipalException;

    /**
     * Returns all principals assignable from the specified type that is associated with
     * this <tt>SecurityContext<tt>.
     * @param principalType the principal type that should be returned.
     * @return a collection of principals that are assignable from the specified type, or
     * an empty collection if no principals of this type are associated.
     */
    Collection<Principal> getAllPrincipalsByType( Class principalType );

    /**
     * Checks if the given role identifier is associated with this context.
     * @param roleIdentifier the role identifier that is being checked.
     * @return true if the user associated with this context has the role, false otherwise.
     */
    boolean hasRole( String roleIdentifier );

    /**
     * Checks a set of role identifiers to see if they are associated with this
     * context and returns a boolean array indicating which roles are associated
     * with this context.
     *
     * <p>This is primarily a performance-enhancing method to help reduce the number of
     * {@link #hasRole} invocations over the wire in client/server systems.
     *
     * @param roleIdentifiers the role identifiers to check for.
     * @return an array of booleans whose indices correspond to the index of the
     * roles in the given identifiers.  A true value indicates the user has the
     * role at that index.  False indicates the user does not have the role.
     */
    boolean[] hasRoles( List<String> roleIdentifiers );

    /**
     * Checks if the user has all of the given roles.
     * @param roleIdentifiers the roles to be checked.
     * @return true if the user has all roles, false otherwise.
     */
    boolean hasAllRoles( Collection<String> roleIdentifiers );

    /**
     * Checks if the given permission is associated with this context.
     * @param permission the permission that is being checked.
     * @return true if the user associated with this context implies the permission, false otherwise.
     */
    boolean implies( Permission permission );

    /**
     * Checks a set of permissions to see if they are implied by this
     * context and returns a boolean array indicating which permissions are implied by this context.
     *
     * <p>This is primarily a performance-enhancing method to help reduce the number of
     * {@link #implies} invocations over the wire in client/server systems.
     *
     * @param permissions the permissions to check for.
     * @return an array of booleans whose indices correspond to the index of the
     * permissions in the given list.  A true value indicates the permission is implied by the user
     * associated with this context.  A false value indicates the permission is not implied.
     */
    boolean[] implies( List<Permission> permissions );

    /**
     * Checks if the user has all of the given permissions.
     * @param permissions the permissions to be checked.
     * @return true if the user has all permissions, false otherwise.
     */
    boolean impliesAll( Collection<Permission> permissions );


    /**
     * A convenience method to check if this context implies the specified permission.  That is,
     * if the authorization context does not imply the given permission, an
     * {@link org.jsecurity.authz.AuthorizationException} will be thrown.
     * @param permission the permission to check.
     * @throws org.jsecurity.authz.AuthorizationException if the user does not have the permission.
     */
    void checkPermission( Permission permission ) throws AuthorizationException;


    /**
     * A convenience method for checking if this context implies all of the specified permissions.
     * @param permissions the permissions to check.
     * @throws AuthorizationException if the context does not imply all of the given permissions.
     */
    void checkPermissions( Collection<Permission> permissions ) throws AuthorizationException;

    /**
     * Returns <tt>true</tt> if the user represented by this <tt>SecurityContxt</tt> is currently
     * logged-in to the system, <tt>false</tt> otherwise.
     * @return <tt>true</tt> if the user represented by this <tt>SecurityContxt</tt> is currently
     * logged-in to the system, <tt>false</tt> otherwise.
     */
    public abstract boolean isAuthenticated();

    /**
     * Returns the user's application <tt>Session</tt>, or <tt>null</tt> if there is no
     * <tt>Session</tt> associated with the user.
     *
     * @return the user's application <tt>Session</tt>, or <tt>null</tt>
     * if there is no session associated with the user.
     */
    public abstract Session getSession();

    /**
     * Invalidates and removes any entities (such as a {@link Session Session} and authorization
     * context associated with this <tt>SecurityContext</tt>.
     *
     * @see #getSession
     */
    public abstract void invalidate();

}
