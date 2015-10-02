precision mediump float;

uniform sampler2D u_TextureUnit;
uniform float u_Transparency;

varying vec2 v_TextureCoordinates;

vec4 color;

void main()
{
	color = texture2D(u_TextureUnit, v_TextureCoordinates);
	color.a *= u_Transparency;
	gl_FragColor = color;
	
}
