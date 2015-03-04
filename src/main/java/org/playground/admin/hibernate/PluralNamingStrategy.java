package org.playground.admin.hibernate;

import org.hibernate.AssertionFailure;
import org.hibernate.cfg.ImprovedNamingStrategy;
import org.hibernate.internal.util.StringHelper;
import org.jvnet.inflector.Noun;

/**
 * Hibernate naming strategy that extends from {@link org.hibernate.cfg.ImprovedNamingStrategy}, but
 * uses plural table names.
 */
public class PluralNamingStrategy extends ImprovedNamingStrategy {

    private static final long serialVersionUID = 3615635979868113529L;


    /**
     * Produces a plural table name from the given class name.
     *
     * @param className the class name for which a table name should be generated.
     * @return a pluralized version of the class name using underscores instead of mixed case.
     */
    public String classToTableName(String className) {
        return Noun.pluralOf(super.classToTableName(className));
    }

    /**
     * Return the property name or propertyTableName
     */
    public String foreignKeyColumnName(String propertyName, String propertyEntityName, String propertyTableName, String referencedColumnName) {
        String header = propertyName != null ? StringHelper.unqualify(propertyName) : propertyTableName;
        if (header == null) {
            throw new AssertionFailure("NamingStrategy not properly filled");
        }
        return columnName(header) + "_" + referencedColumnName;
    }

}
