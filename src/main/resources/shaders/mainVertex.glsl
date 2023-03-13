#version 460 core

in vec3 position;
in vec4 color;
in vec2 textureCoord;

out vec4 passColor;
out vec2 passTextureCoord;

uniform mat4 model;
uniform mat4 view;
uniform mat4 projection;
uniform mat4 forward;
uniform mat4 back;

void main() {
	gl_Position = projection * view * (back * model * forward * vec4(position, 1.0));
	passColor = color;
	passTextureCoord = textureCoord;
}