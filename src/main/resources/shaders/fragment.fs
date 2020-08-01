#version 420

const int MAX_POINT_LIGHTS = 8;
const int MAX_SPOT_LIGHTS = 8;

in vec2 fragTextureCoord;
in vec3 fragMVVertexNormal;
in vec3 fragMVVertexPos;

out vec4 fragColor;

struct Attenuation
{
                        //базовое выравнивание      //веровненное смещение
    float constant;     //4                         //0
    float linear;       //4                         //4
    float exponent;     //4                         //8
            //размер:   //16
};

struct PointLight
{
                        //базовое выравнивание      //веровненное смещение
    vec3 colour;        //16                        //0
    // Light position is assumed to be in view coordinates
    vec3 position;      //16                        //16
    float intensity;    //4                         //28
    Attenuation att;    //16                        //32
            //размер:   //48
};

struct SpotLight
{
                        //базовое выравнивание      //веровненное смещение
    vec3 conedir;       //16                        //0
    float cutoff;       //4                         //12
    PointLight pl;      //48                        //16
            //размер:   //64
};

struct DirectionalLight
{
                        //базовое выравнивание      //веровненное смещение
    vec3 colour;        //16                        //0
    vec3 direction;     //16                        //16
    float intensity;    //4                         //28
            //размер:   //32
};

struct Material
{
    vec4 ambient;
    vec4 diffuse;
    vec4 specular;
    int hasTexture;
    float reflectance;
};

layout (std140, binding = 2) uniform Lights
{
                                                        //базовое выравнивание      //веровненное смещение
    uniform vec3 ambientLight;                          //16                        //0
    uniform float specularPower;                        //4                         //12
    uniform DirectionalLight directionalLight;          //32                        //16
    uniform PointLight pointLights[MAX_POINT_LIGHTS];   //MAX_POINT_LIGHTS * 48     //48
    uniform SpotLight spotLights[MAX_SPOT_LIGHTS];      //MAX_SPOT_LIGHTS * 64     //80 + MAX_POINT_LIGHTS * 48
                                            //размер:   //80 + MAX_POINT_LIGHTS * 48 + MAX_SPOT_LIGHTS * 64
};

uniform sampler2D texture_sampler;
uniform Material material;

vec4 ambientC;
vec4 diffuseC;
vec4 speculrC;

void setupColours(Material material, vec2 textCoord)
{
    if (material.hasTexture == 1)
    {
        ambientC = texture(texture_sampler, textCoord);
        diffuseC = ambientC;
        speculrC = ambientC;
    }
    else
    {
        ambientC = material.ambient;
        diffuseC = material.diffuse;
        speculrC = material.specular;
    }
}

vec4 calcLightColour(vec3 light_colour, float light_intensity, vec3 position, vec3 to_light_dir, vec3 normal)
{
    vec4 diffuseColour = vec4(0, 0, 0, 0);
    vec4 specColour = vec4(0, 0, 0, 0);

    // Diffuse Light
    float diffuseFactor = max(dot(normal, to_light_dir), 0.0);
    diffuseColour = diffuseC * vec4(light_colour, 1.0) * light_intensity * diffuseFactor;

    // Specular Light
    vec3 camera_direction = normalize(-position);
    vec3 from_light_dir = -to_light_dir;
    vec3 reflected_light = normalize(reflect(from_light_dir , normal));
    float specularFactor = max( dot(camera_direction, reflected_light), 0.0);
    specularFactor = pow(specularFactor, specularPower);
    specColour = speculrC * light_intensity  * specularFactor * material.reflectance * vec4(light_colour, 1.0);

    return (diffuseColour + specColour);
}

vec4 calcPointLight(PointLight light, vec3 position, vec3 normal)
{
    vec3 light_direction = light.position - position;
    vec3 to_light_dir  = normalize(light_direction);
    vec4 light_colour = calcLightColour(light.colour, light.intensity, position, to_light_dir, normal);

    // Apply Attenuation
    float distance = length(light_direction);
    float attenuationInv = light.att.constant + light.att.linear * distance +
        light.att.exponent * distance * distance;
    return light_colour / attenuationInv;
}

vec4 calcSpotLight(SpotLight light, vec3 position, vec3 normal)
{
    vec3 light_direction = light.pl.position - position;
    vec3 to_light_dir  = normalize(light_direction);
    vec3 from_light_dir  = -to_light_dir;
    float spot_alfa = dot(from_light_dir, normalize(light.conedir));

    vec4 colour = vec4(0, 0, 0, 0);

    if ( spot_alfa > light.cutoff )
    {
        colour = calcPointLight(light.pl, position, normal);
        colour *= (1.0 - (1.0 - spot_alfa)/(1.0 - light.cutoff));
    }
    return colour;
}

vec4 calcDirectionalLight(DirectionalLight light, vec3 position, vec3 normal)
{
    return calcLightColour(light.colour, light.intensity, position, normalize(light.direction), normal);
}

void main()
{
    setupColours(material, fragTextureCoord);

    vec4 diffuseSpecularComp = calcDirectionalLight(directionalLight, fragMVVertexPos, fragMVVertexNormal);

    for (int i=0; i<MAX_POINT_LIGHTS; i++)
    {
        if ( pointLights[i].intensity > 0 )
        {
            diffuseSpecularComp += calcPointLight(pointLights[i], fragMVVertexPos, fragMVVertexNormal);
        }
    }

    for (int i=0; i<MAX_SPOT_LIGHTS; i++)
    {
        if ( spotLights[i].pl.intensity > 0 )
        {
            diffuseSpecularComp += calcSpotLight(spotLights[i], fragMVVertexPos, fragMVVertexNormal);
        }
    }

    fragColor = ambientC * vec4(ambientLight, 1) + diffuseSpecularComp;
}