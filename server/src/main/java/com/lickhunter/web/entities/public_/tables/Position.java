/*
 * This file is generated by jOOQ.
 */
package com.lickhunter.web.entities.public_.tables;


import com.lickhunter.web.entities.public_.Indexes;
import com.lickhunter.web.entities.public_.Keys;
import com.lickhunter.web.entities.public_.Public;
import com.lickhunter.web.entities.public_.tables.records.PositionRecord;

import java.util.Arrays;
import java.util.List;

import javax.annotation.Generated;

import org.jooq.Field;
import org.jooq.ForeignKey;
import org.jooq.Index;
import org.jooq.Name;
import org.jooq.Record;
import org.jooq.Row12;
import org.jooq.Schema;
import org.jooq.Table;
import org.jooq.TableField;
import org.jooq.UniqueKey;
import org.jooq.impl.DSL;
import org.jooq.impl.TableImpl;


/**
 * This class is generated by jOOQ.
 */
@Generated(
    value = {
        "http://www.jooq.org",
        "jOOQ version:3.12.4"
    },
    comments = "This class is generated by jOOQ"
)
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class Position extends TableImpl<PositionRecord> {

    private static final long serialVersionUID = 433488927;

    /**
     * The reference instance of <code>PUBLIC.POSITION</code>
     */
    public static final Position POSITION = new Position();

    /**
     * The class holding records for this type
     */
    @Override
    public Class<PositionRecord> getRecordType() {
        return PositionRecord.class;
    }

    /**
     * The column <code>PUBLIC.POSITION.ISOLATED</code>.
     */
    public final TableField<PositionRecord, Boolean> ISOLATED = createField(DSL.name("ISOLATED"), org.jooq.impl.SQLDataType.BOOLEAN, this, "");

    /**
     * The column <code>PUBLIC.POSITION.LEVERAGE</code>.
     */
    public final TableField<PositionRecord, Double> LEVERAGE = createField(DSL.name("LEVERAGE"), org.jooq.impl.SQLDataType.DOUBLE, this, "");

    /**
     * The column <code>PUBLIC.POSITION.INITIAL_MARGIN</code>.
     */
    public final TableField<PositionRecord, Double> INITIAL_MARGIN = createField(DSL.name("INITIAL_MARGIN"), org.jooq.impl.SQLDataType.DOUBLE.nullable(false), this, "");

    /**
     * The column <code>PUBLIC.POSITION.MAINT_MARGIN</code>.
     */
    public final TableField<PositionRecord, Double> MAINT_MARGIN = createField(DSL.name("MAINT_MARGIN"), org.jooq.impl.SQLDataType.DOUBLE, this, "");

    /**
     * The column <code>PUBLIC.POSITION.OPEN_ORDER_INITIAL_MARGIN</code>.
     */
    public final TableField<PositionRecord, Double> OPEN_ORDER_INITIAL_MARGIN = createField(DSL.name("OPEN_ORDER_INITIAL_MARGIN"), org.jooq.impl.SQLDataType.DOUBLE, this, "");

    /**
     * The column <code>PUBLIC.POSITION.POSITION_INITIAL_MARGIN</code>.
     */
    public final TableField<PositionRecord, Double> POSITION_INITIAL_MARGIN = createField(DSL.name("POSITION_INITIAL_MARGIN"), org.jooq.impl.SQLDataType.DOUBLE.nullable(false), this, "");

    /**
     * The column <code>PUBLIC.POSITION.SYMBOL</code>.
     */
    public final TableField<PositionRecord, String> SYMBOL = createField(DSL.name("SYMBOL"), org.jooq.impl.SQLDataType.VARCHAR(20).nullable(false), this, "");

    /**
     * The column <code>PUBLIC.POSITION.UNREALIZED_PROFIT</code>.
     */
    public final TableField<PositionRecord, Double> UNREALIZED_PROFIT = createField(DSL.name("UNREALIZED_PROFIT"), org.jooq.impl.SQLDataType.DOUBLE.nullable(false), this, "");

    /**
     * The column <code>PUBLIC.POSITION.ENTRY_PRICE</code>.
     */
    public final TableField<PositionRecord, String> ENTRY_PRICE = createField(DSL.name("ENTRY_PRICE"), org.jooq.impl.SQLDataType.VARCHAR(255).nullable(false), this, "");

    /**
     * The column <code>PUBLIC.POSITION.MAX_NOTIONAL</code>.
     */
    public final TableField<PositionRecord, String> MAX_NOTIONAL = createField(DSL.name("MAX_NOTIONAL"), org.jooq.impl.SQLDataType.VARCHAR(255), this, "");

    /**
     * The column <code>PUBLIC.POSITION.POSITION_SIDE</code>.
     */
    public final TableField<PositionRecord, String> POSITION_SIDE = createField(DSL.name("POSITION_SIDE"), org.jooq.impl.SQLDataType.VARCHAR(255), this, "");

    /**
     * The column <code>PUBLIC.POSITION.ACCOUNT_ID</code>.
     */
    public final TableField<PositionRecord, String> ACCOUNT_ID = createField(DSL.name("ACCOUNT_ID"), org.jooq.impl.SQLDataType.VARCHAR(255).nullable(false), this, "");

    /**
     * Create a <code>PUBLIC.POSITION</code> table reference
     */
    public Position() {
        this(DSL.name("POSITION"), null);
    }

    /**
     * Create an aliased <code>PUBLIC.POSITION</code> table reference
     */
    public Position(String alias) {
        this(DSL.name(alias), POSITION);
    }

    /**
     * Create an aliased <code>PUBLIC.POSITION</code> table reference
     */
    public Position(Name alias) {
        this(alias, POSITION);
    }

    private Position(Name alias, Table<PositionRecord> aliased) {
        this(alias, aliased, null);
    }

    private Position(Name alias, Table<PositionRecord> aliased, Field<?>[] parameters) {
        super(alias, null, aliased, parameters, DSL.comment(""));
    }

    public <O extends Record> Position(Table<O> child, ForeignKey<O, PositionRecord> key) {
        super(child, key, POSITION);
    }

    @Override
    public Schema getSchema() {
        return Public.PUBLIC;
    }

    @Override
    public List<Index> getIndexes() {
        return Arrays.<Index>asList(Indexes.FK_POSITION_ACCOUNT_ID_INDEX_5, Indexes.PRIMARY_KEY_5);
    }

    @Override
    public UniqueKey<PositionRecord> getPrimaryKey() {
        return Keys.PK_POSITION;
    }

    @Override
    public List<UniqueKey<PositionRecord>> getKeys() {
        return Arrays.<UniqueKey<PositionRecord>>asList(Keys.PK_POSITION);
    }

    @Override
    public List<ForeignKey<PositionRecord, ?>> getReferences() {
        return Arrays.<ForeignKey<PositionRecord, ?>>asList(Keys.FK_POSITION_ACCOUNT_ID);
    }

    public Account account() {
        return new Account(this, Keys.FK_POSITION_ACCOUNT_ID);
    }

    @Override
    public Position as(String alias) {
        return new Position(DSL.name(alias), this);
    }

    @Override
    public Position as(Name alias) {
        return new Position(alias, this);
    }

    /**
     * Rename this table
     */
    @Override
    public Position rename(String name) {
        return new Position(DSL.name(name), null);
    }

    /**
     * Rename this table
     */
    @Override
    public Position rename(Name name) {
        return new Position(name, null);
    }

    // -------------------------------------------------------------------------
    // Row12 type methods
    // -------------------------------------------------------------------------

    @Override
    public Row12<Boolean, Double, Double, Double, Double, Double, String, Double, String, String, String, String> fieldsRow() {
        return (Row12) super.fieldsRow();
    }
}
