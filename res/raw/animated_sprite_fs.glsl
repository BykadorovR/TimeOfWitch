precision mediump float; 
      	 				
uniform sampler2D u_TextureUnit;      	 								
varying vec2 v_TextureCoordinates;   
uniform float u_AdjustX;
uniform float u_AdjustY;   	   								
  
void main()                    		
{                              	
    gl_FragColor = texture2D(u_TextureUnit, vec2(v_TextureCoordinates.x+u_AdjustX, v_TextureCoordinates.y+u_AdjustY));                           		
}