package com.thomasjensen.boxes.online;

import java.util.ArrayList;
import java.util.List;

import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;


/**
 * Construct a complete <i>boxes</i> command line from a JSON {@link Invocation}.
 */
public class CommandLineBuilder
{
    private final List<String> cmdLine;



    public CommandLineBuilder()
    {
        cmdLine = new ArrayList<>();
        cmdLine.add("boxes");
        //cmdLine.add("-q");    // TODO
    }



    @NonNull
    public List<String> build(@NonNull final Invocation pInvocation)
    {
        if (pInvocation.isVersion()) {
            cmdLine.add("-v");
        }
        else if (pInvocation.isListDesigns()) {
            cmdLine.add("-l");
            design(pInvocation.getDesign());
        }
        else {
            alignment(pInvocation.getAlignment());
            design(pInvocation.getDesign());
            indentationMode(pInvocation.getIndentationMode());
            padding(pInvocation.getPadding());
            boxSize(pInvocation.getSize());
            tabHandling(pInvocation.getTabHandling());
        }
        return cmdLine;
    }



    private void alignment(@Nullable final Invocation.Alignment pAlignment)
    {
        StringBuilder sb = new StringBuilder();
        if (pAlignment != null) {
            alignmentFragment(sb, 'h', pAlignment.getHorizontal());
            alignmentFragment(sb, 'v', pAlignment.getVertical());
            alignmentFragment(sb, 'j', pAlignment.getJustification());
        }
        if (sb.length() > 0) {
            cmdLine.add("-a");
            cmdLine.add(sb.toString());
        }
    }



    private void alignmentFragment(@NonNull final StringBuilder pSb, final char pCode,
        @Nullable final ArgumentValue pArgumentValue)
    {
        if (pArgumentValue != null) {
            pSb.append(pCode);
            pSb.append(pArgumentValue.getShortForm());
        }
    }



    private void design(@Nullable final String pDesign)
    {
        if (pDesign != null) {
            cmdLine.add("-d");
            cmdLine.add(pDesign);
        }
    }



    private void indentationMode(@Nullable final IndentationMode pIndentationMode)
    {
        if (pIndentationMode != null) {
            cmdLine.add("-i");
            cmdLine.add(pIndentationMode.getArgumentString());
        }
    }



    private void padding(@Nullable final Invocation.Padding pPadding)
    {
        StringBuilder sb = new StringBuilder();
        if (pPadding != null) {
            paddingFragment(sb, 't', pPadding.getTop());
            paddingFragment(sb, 'r', pPadding.getRight());
            paddingFragment(sb, 'b', pPadding.getBottom());
            paddingFragment(sb, 'l', pPadding.getLeft());
        }
        if (sb.length() > 0) {
            cmdLine.add("-p");
            cmdLine.add(sb.toString());
        }
    }



    private void paddingFragment(@NonNull final StringBuilder pSb, final char pCode, final int pValue)
    {
        if (pValue > Invocation.Padding.NOT_SET) {
            pSb.append(pCode);
            pSb.append(pValue);
        }
    }



    private void boxSize(@Nullable final Invocation.Size pSize)
    {
        if (pSize != null) {
            cmdLine.add("-s");
            cmdLine.add(pSize.getWidth() + "x" + pSize.getHeight());
        }
    }



    private void tabHandling(@Nullable final Invocation.Tabs pTabHandling)
    {
        if (pTabHandling != null) {
            String arg = String.valueOf(pTabHandling.getDistance());
            if (pTabHandling.getLeadingTabs() != null) {
                arg += pTabHandling.getLeadingTabs().getShortForm();
            }
            cmdLine.add("-t");
            cmdLine.add(arg);
        }
    }
}
