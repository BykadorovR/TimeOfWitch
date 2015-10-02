/***
 * Excerpted from "OpenGL ES for Android",
 * published by The Pragmatic Bookshelf.
 * Copyrights apply to this code. It may not be used to create training material, 
 * courses, books, articles, and the like. Contact us if you are in doubt.
 * We make no guarantees that this code is fit for any purpose. 
 * Visit http://www.pragmaticprogrammer.com/titles/kbogla for more book information.
***/
package com.timeOfWitch.android;


public class Constants {
    public static final int BYTES_PER_FLOAT = 4;
    public static final int POSITION_COMPONENT_COUNT = 2;
    public static final int TEXTURE_COORDINATES_COMPONENT_COUNT = 2;
    public static final int STRIDE = (POSITION_COMPONENT_COUNT 
        + TEXTURE_COORDINATES_COMPONENT_COUNT) * BYTES_PER_FLOAT;
    public static final int VECTOR_COMPONENT_COUNT = 3;    
    public static final int COLOR_COMPONENT_COUNT = 3;
    public static final int PARTICLE_START_TIME_COMPONENT_COUNT = 1;
    public static final int TOTAL_COMPONENT_COUNT = 
        POSITION_COMPONENT_COUNT
      + COLOR_COMPONENT_COUNT 
      + VECTOR_COMPONENT_COUNT      
      + PARTICLE_START_TIME_COMPONENT_COUNT;
    public static final int STRIDE_PARTICLE = TOTAL_COMPONENT_COUNT * BYTES_PER_FLOAT;
}
