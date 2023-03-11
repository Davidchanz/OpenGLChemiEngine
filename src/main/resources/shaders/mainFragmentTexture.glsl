#version 330 core

in vec3 passColor;
in vec2 passTextureCoord;

out vec4 outColor;

uniform sampler2D texture0;

void main() {
	outColor = texture(texture0, passTextureCoord) * vec4(passColor, 1.0f);
	/**if(passColor.x == 1.0f){
		outColor = texture(texture4, passTextureCoord);
	}*/
	/**if(passColor.x == 0.0f){
		outColor = texture(texture20, passTextureCoord);
	}*/
	//outColor = vec4(passColor, 1.0);

}