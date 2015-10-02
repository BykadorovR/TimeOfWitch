precision mediump float;

uniform sampler2D u_TextureUnit;
varying vec2 v_TextureCoordinates;
uniform float u_Slide;

void main()
{

	gl_FragColor = texture2D(u_TextureUnit, vec2(v_TextureCoordinates.x+u_Slide, v_TextureCoordinates.y));
}
