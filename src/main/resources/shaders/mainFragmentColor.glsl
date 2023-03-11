#version 330 core

in vec3 passColor;
in vec2 passTextureCoord;

out vec4 outColor;

uniform sampler2D texture0;
//uniform sampler2D texture1;
/**uniform sampler2D texture8;
uniform sampler2D texture12;
uniform sampler2D texture16;
uniform sampler2D texture20;
uniform sampler2D texture24;*/

void main() {
	outColor = vec4(passColor, 1.0f);
	/**if(passColor.x == 1.0f){
		outColor = texture(texture4, passTextureCoord);
	}*/
	/**if(passColor.x == 0.0f){
		outColor = texture(texture20, passTextureCoord);
	}*/
	//outColor = vec4(passColor, 1.0);

}