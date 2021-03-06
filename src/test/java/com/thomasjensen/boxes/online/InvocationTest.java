package com.thomasjensen.boxes.online;
/*
 * boxes-online - A Web UI for the 'boxes' tool
 * Copyright (C) 2018  Thomas Jensen and the contributors
 *
 * This program is free software; you can redistribute it and/or modify it under the terms of the GNU General Public
 * License as published by the Free Software Foundation; either version 2 of the License, or (at your option) any later
 * version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied
 * warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License for more
 * details.
 *
 * You should have received a copy of the GNU General Public License along with this program; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301, USA.
 */

import java.io.IOException;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import org.junit.Assert;
import org.junit.Test;


/**
 * Some unit tests for the {@link Invocation} class.
 */
public class InvocationTest
{
    @Test
    public void testDeserializationFull()
        throws IOException
    {
        final String json = "{\"alignment\": {\"horizontal\": \"center\", \"vertical\": \"center\"}, "
            + "\"design\": \"parchment\", \"padding\": {\"top\": 1, \"right\": 2, \"bottom\": 1, \"left\": 2}, "
            + "\"size\": {\"width\": 42, \"height\": 10}, \"tabDistance\": 4, \"content\": \"unit test\"}";
        final Invocation underTest = new ObjectMapper().readValue(json, Invocation.class);

        Assert.assertNotNull(underTest);
        Assert.assertNotNull(underTest.getAlignment());
        Assert.assertEquals(HorzAlign.Center, underTest.getAlignment().getHorizontal());
        Assert.assertEquals(VertAlign.Center, underTest.getAlignment().getVertical());
        Assert.assertEquals(HorzAlign.Left, underTest.getAlignment().getJustification());
        Assert.assertEquals("parchment", underTest.getDesign());
        Assert.assertNotNull(underTest.getSize());
        Assert.assertEquals(42, underTest.getSize().getWidth());
        Assert.assertEquals(10, underTest.getSize().getHeight());
        Assert.assertEquals(4, underTest.getTabDistance());
        Assert.assertEquals("unit test", underTest.getContent());
    }



    @Test
    public void testDeserializationContentOnly()
        throws IOException
    {
        final String json = "{\"content\": \"unit test\"}";
        final Invocation underTest = new ObjectMapper().readValue(json, Invocation.class);

        Assert.assertNotNull(underTest);
        Assert.assertNull(underTest.getAlignment());
        Assert.assertNull(underTest.getDesign());
        Assert.assertNull(underTest.getSize());
        Assert.assertEquals(8, underTest.getTabDistance());
        Assert.assertEquals("unit test", underTest.getContent());
    }



    @Test(expected = InvalidFormatException.class)
    public void testInvalidJson()
        throws IOException
    {
        final String json = "{\"alignment\": {\"horizontal\": \"INVALID\"}, \"content\": \"unit test\"}";
        new ObjectMapper().readValue(json, Invocation.class);
        Assert.fail("expected InvalidFormatException was not thrown");
    }



    @Test
    public void testSerialization()
        throws IOException
    {
        final Invocation original = new Invocation();
        Invocation.Alignment alignment = new Invocation.Alignment();
        alignment.setHorizontal(HorzAlign.Center);
        alignment.setVertical(VertAlign.Bottom);
        alignment.setJustification(HorzAlign.Left);
        original.setAlignment(alignment);
        original.setDesign("dog");
        Invocation.Padding padding = new Invocation.Padding();
        padding.setTop(1);
        padding.setRight(2);
        padding.setBottom(3);
        padding.setLeft(4);
        original.setPadding(padding);
        Invocation.Size size = new Invocation.Size();
        size.setWidth(42);
        size.setHeight(10);
        original.setSize(size);
        original.setTabDistance(4);
        original.setContent("unit test");

        final String serialized = new ObjectMapper().writeValueAsString(original);
        final Invocation deserialized = new ObjectMapper().readValue(serialized, Invocation.class);

        Assert.assertNotNull(deserialized);
    }
}
