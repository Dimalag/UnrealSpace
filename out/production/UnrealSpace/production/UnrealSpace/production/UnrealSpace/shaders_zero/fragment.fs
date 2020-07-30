#version 460

out vec4 outTexCoord;
in vec2 fragTextureCoord;

uniform sampler2D textureSampler;

void main()
{
    outTexCoord = texture(textureSampler, fragTextureCoord);
}