#version 330 core

in vec4 passColor;
in vec2 passTextureCoord;

out vec4 outColor;

uniform sampler2D texture0;

void main() {
	if (texture(texture0, passTextureCoord).a < 0.1)
		discard;
	outColor = texture(texture0, passTextureCoord) * passColor/**vec4(passColor.r,passColor.g, passColor.b, 0.8f)*/;
	/**if(passColor.x == 1.0f){
		outColor = texture(texture4, passTextureCoord);
	}*/
	/**if(passColor.x == 0.0f){
		outColor = texture(texture20, passTextureCoord);
	}*/
	//outColor = passColor;

}