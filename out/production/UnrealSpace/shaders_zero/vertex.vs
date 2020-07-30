#version 460

layout (location =0) in vec3 position;
layout (location =1) in vec2 inTextureCoord;

out vec2 fragTextureCoord;

uniform int isPerson;
uniform mat4 model;
uniform mat4 transform;
uniform mat4 view;
uniform mat4 projection;

void main()
{
    fragTextureCoord = inTextureCoord;
    if (isPerson == 0)
        gl_Position = projection * view * transform * model * vec4(position, 1.0); // drawing objects
    else if (isPerson == 1)
        gl_Position = projection * model * vec4(position, 1.0); // drawing person
}