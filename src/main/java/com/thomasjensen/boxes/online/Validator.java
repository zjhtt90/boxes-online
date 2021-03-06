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

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
import org.springframework.util.SerializationUtils;


/**
 * Validates and optionally fixes an {@link Invocation}.
 */
@Component
public class Validator
{
    /** min. width that can be specified on the <i>boxes</i> command line */
    private static final int MIN_WIDTH = 1;

    /** max. characters per line, from {@code boxes.h.in} */
    private static final int MAX_WIDTH = 2048;

    /** min. height that can be specified on the <i>boxes</i> command line */
    private static final int MIN_HEIGHT = 1;

    /** max. number of input lines. Ten times that works too, but this limit constrains CPU and network time. */
    private static final int MAX_HEIGHT = 10000;

    /** min. tabstop distance that can be set on the <i>boxes</i> command line */
    private static final int MIN_TABSIZE = 1;

    /** max. tab stop distance, from {@code boxes.h.in} */
    private static final int MAX_TABSIZE = 16;

    /** max. length of an accepted box design name */
    private static final int MAX_DESIGN_NAME_LEN = 80;

    private final DesignList designList;



    @Autowired
    public Validator(final DesignList pDesignList)
    {
        Assert.notNull(pDesignList, "DesignList not injected");
        designList = pDesignList;
    }



    public void validate(@NonNull final Invocation pInvocation)
        throws InvalidInvocationException
    {
        Assert.notNull(pInvocation, "Argument pInvocation is null");
        execute(pInvocation, true);
    }



    public Invocation checkup(@NonNull final Invocation pInvocation)
    {
        Assert.notNull(pInvocation, "Argument pInvocation is null");
        Invocation copyInvocation = (Invocation) SerializationUtils.deserialize(
            SerializationUtils.serialize(pInvocation));
        assert copyInvocation != null;
        try {
            return execute(copyInvocation, false);
        }
        catch (InvalidInvocationException e) {
            throw new IllegalStateException("bug in execute()", e);
        }
    }



    private Invocation execute(@NonNull final Invocation pInvocation, final boolean pThrowEx)
        throws InvalidInvocationException
    {
        handleDesignName(pInvocation.getDesign());
        handleBoxSize(pInvocation.getSize(), pThrowEx);
        handlePadding(pInvocation.getPadding());
        handleTabs(pInvocation, pThrowEx);
        handleContent(pInvocation);
        return pInvocation;
    }



    private void handleDesignName(@Nullable final String pDesignName)
        throws InvalidInvocationException
    {
        if (pDesignName != null && pDesignName.length() > MAX_DESIGN_NAME_LEN) {
            throw new InvalidInvocationException("Specified design name too long");
        }
        if (!designList.isSupported(pDesignName)) {
            throw new InvalidInvocationException("Specified design does not exist");
        }
    }



    private void handleBoxSize(@Nullable final Invocation.Size pSize, final boolean pThrowEx)
        throws InvalidInvocationException
    {
        if (pSize != null) {
            checkAndCorrect(pSize.getWidth() < MIN_WIDTH, () -> pSize.setWidth(MIN_WIDTH));
            if (pSize.getWidth() > MAX_WIDTH) {
                handleViolation(pThrowEx, () -> pSize.setWidth(MAX_WIDTH),
                    "Box width of " + pSize.getWidth() + " exceeds maximum width of " + MAX_WIDTH);
            }

            checkAndCorrect(pSize.getHeight() < MIN_HEIGHT, () -> pSize.setHeight(MIN_HEIGHT));
            if (pSize.getHeight() > MAX_HEIGHT) {
                handleViolation(pThrowEx, () -> pSize.setHeight(MAX_HEIGHT),
                    "Box height of " + pSize.getHeight() + " exceeds maximum height of " + MAX_HEIGHT);
            }
        }
    }



    private void handlePadding(@Nullable final Invocation.Padding pPadding)
        throws InvalidInvocationException
    {
        if (pPadding != null) {
            checkAndCorrect(pPadding.getTop() < -1, () -> pPadding.setTop(Invocation.Padding.NOT_SET));
            checkAndCorrect(pPadding.getRight() < -1, () -> pPadding.setRight(Invocation.Padding.NOT_SET));
            checkAndCorrect(pPadding.getBottom() < -1, () -> pPadding.setBottom(Invocation.Padding.NOT_SET));
            checkAndCorrect(pPadding.getLeft() < -1, () -> pPadding.setLeft(Invocation.Padding.NOT_SET));
        }
    }



    private void handleTabs(@NonNull final Invocation pInvocation, final boolean pThrowEx)
        throws InvalidInvocationException
    {
        checkAndCorrect(pInvocation.getTabDistance() < MIN_TABSIZE, () -> pInvocation.setTabDistance(MIN_TABSIZE));
        if (pInvocation.getTabDistance() > MAX_TABSIZE) {
            handleViolation(pThrowEx, () -> pInvocation.setTabDistance(MAX_TABSIZE),
                "Tab distance of " + pInvocation.getTabDistance() + " exceeds maximum tab distance of " + MAX_TABSIZE);
        }
    }



    private void handleContent(final Invocation pInvocation)
        throws InvalidInvocationException
    {
        //noinspection ConstantConditions
        if (pInvocation.getContent() == null || pInvocation.getContent().isBlank()) {
            throw new InvalidInvocationException("no box content specified");
        }
    }



    private void checkAndCorrect(final boolean pCondition, final Runnable pCompensation)
        throws InvalidInvocationException
    {
        if (pCondition) {
            handleViolation(false, pCompensation, null);
        }
    }



    private void handleViolation(final boolean pThrowEx, final Runnable pCompensation, final String pExMessage)
        throws InvalidInvocationException
    {
        if (pThrowEx) {
            throw new InvalidInvocationException(pExMessage);
        }
        pCompensation.run();
    }
}
