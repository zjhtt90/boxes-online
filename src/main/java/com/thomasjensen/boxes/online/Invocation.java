package com.thomasjensen.boxes.online;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.lang.Nullable;


/**
 * Describes an invocation of <i>boxes</i> with all the parameters.
 */
public class Invocation
    implements Serializable
{
    @JsonProperty
    private Alignment alignment;

    @JsonProperty
    private String design;

    @JsonProperty
    private Padding padding;

    @JsonProperty
    private Size size;

    @JsonProperty
    private int tabDistance = 8;

    @JsonProperty
    private boolean version;



    public static class Alignment
        implements Serializable
    {
        @Nullable
        @JsonProperty
        private HorzAlign horizontal = HorzAlign.Left;

        @Nullable
        @JsonProperty
        private VertAlign vertical = VertAlign.Top;

        @Nullable
        @JsonProperty
        private HorzAlign justification = HorzAlign.Left;



        @Nullable
        public HorzAlign getHorizontal()
        {
            return horizontal;
        }



        public void setHorizontal(@Nullable final HorzAlign pHorizontal)
        {
            horizontal = pHorizontal;
        }



        @Nullable
        public VertAlign getVertical()
        {
            return vertical;
        }



        public void setVertical(@Nullable final VertAlign pVertical)
        {
            vertical = pVertical;
        }



        @Nullable
        public HorzAlign getJustification()
        {
            return justification;
        }



        public void setJustification(@Nullable final HorzAlign pJustification)
        {
            justification = pJustification;
        }
    }



    public static class Padding
        implements Serializable
    {
        /** no padding specified -> use default from box design */
        public static final int NOT_SET = -1;

        @JsonProperty
        private int top = NOT_SET;

        @JsonProperty
        private int right = NOT_SET;

        @JsonProperty
        private int bottom = NOT_SET;

        @JsonProperty
        private int left = NOT_SET;



        public int getTop()
        {
            return top;
        }



        public void setTop(final int pTop)
        {
            top = pTop;
        }



        public int getRight()
        {
            return right;
        }



        public void setRight(final int pRight)
        {
            right = pRight;
        }



        public int getBottom()
        {
            return bottom;
        }



        public void setBottom(final int pBottom)
        {
            bottom = pBottom;
        }



        public int getLeft()
        {
            return left;
        }



        public void setLeft(final int pLeft)
        {
            left = pLeft;
        }
    }



    public static class Size
        implements Serializable
    {
        @JsonProperty
        private int width = 1;

        @JsonProperty
        private int height = 1;



        public int getWidth()
        {
            return width;
        }



        public void setWidth(final int pWidth)
        {
            width = pWidth;
        }



        public int getHeight()
        {
            return height;
        }



        public void setHeight(final int pHeight)
        {
            height = pHeight;
        }
    }



    @Nullable
    public Alignment getAlignment()
    {
        return alignment;
    }



    public void setAlignment(@Nullable final Alignment pAlignment)
    {
        alignment = pAlignment;
    }



    @Nullable
    public String getDesign()
    {
        return design;
    }



    public void setDesign(@Nullable final String pDesign)
    {
        design = pDesign;
    }



    @Nullable
    public Padding getPadding()
    {
        return padding;
    }



    public void setPadding(@Nullable final Padding pPadding)
    {
        padding = pPadding;
    }



    @Nullable
    public Size getSize()
    {
        return size;
    }



    public void setSize(@Nullable final Size pSize)
    {
        size = pSize;
    }



    public int getTabDistance()
    {
        return tabDistance;
    }



    public void setTabDistance(final int pTabDistance)
    {
        tabDistance = pTabDistance;
    }



    public boolean isVersion()
    {
        return version;
    }



    public void setVersion(final boolean pVersion)
    {
        version = pVersion;
    }
}
