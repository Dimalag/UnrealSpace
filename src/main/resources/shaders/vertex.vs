#version 460

layout (location = 0) in vec3 position;
layout (location = 1) in vec2 textureCoord;
layout (location = 2) in vec3 vertexNormal;

out vec2 fragTextureCoord;
out vec3 fragMVVertexNormal;
out vec3 fragMVVertexPos;

const float M_PI = 3.1415926535897932384626433832795;
const int SIMPLE_TRANSFORMATION_MAX_SIZE = 16;
const int COMPLEX_TRANSFORMATION_MAX_SIZE = 16;
const int ALL_TRANSFORMATION_MAX_SIZE = SIMPLE_TRANSFORMATION_MAX_SIZE + COMPLEX_TRANSFORMATION_MAX_SIZE;
const int SIMPLE_MATRIX_TYPE = 1;
const int COMPLEX_ROTATION_TYPE = 2;

const int COMPLEX_TRANSFORMATION_MATRICES_MAX_SIZE = 120; //rotation matrices count in complex effects

struct SimpleTransformation
{
    mat4 transformationMatrix;
    //размер:   //64
};

struct ComplexTransformation
{                                   //базовое выравнивание      //веровненное смещение
    //rotation direction
    vec3 direction;                 //16                        //0
    //distance to full circle rotation - corresponds 2pi
    float distance;                 //4                         //12

    //start location of effect action
    vec3 startLocation;             //16                        //16

    //coefficient from 0 to 1 of effect influence
    float coefficient;              //4                         //28

    //end location of effect action
    vec3 endLocation;               //16                        //32

                                    //16                        //48
    //mat4 rotationMatrices[COMPLEX_TRANSFORMATION_MATRICES_MAX_SIZE]; // complex rotation matrices

    //matrix offset translate
    mat4 offsetTranslate;           //16                        //48+64*COMPLEX_TRANSFORMATION_MATRICES_MAX_SIZE
    //matrix offset translate back
    mat4 offsetTranslateNegative;   //16                        //112+64*COMPLEX_TRANSFORMATION_MATRICES_MAX_SIZE
                //размер:           //176 + 64*COMPLEX_TRANSFORMATION_MATRICES_MAX_SIZE
};

layout (std140, binding = 0) uniform Matrices
{
    uniform mat4 view;
    uniform mat4 projection;
};

layout (std140, binding = 1) uniform TransformationsBlock
{
                                        //базовое выравнивание(размер)                  //веровненное смещение
    // person location
    vec3 personLocation;                //16                                            //0
    int countSimpleTransformations;     //4                                             //12
    int countComplexTransformations;    //4                                             //16

    //count of all transformations, which are in queue independently of type (simple, complex, ...)
    int countTransformations;           //4                                             //20

                                        //16 (16*ALL_TRANSFORMATION_MAX_SIZE)           //32
    int transformationsTypes[ALL_TRANSFORMATION_MAX_SIZE]; //[i] transformation type (simple, complex, ...)
                                        //16 (16*ALL_TRANSFORMATION_MAX_SIZE)           //32+16*ALL_TRANSFORMATION_MAX_SIZE
    int transformationsQueue[ALL_TRANSFORMATION_MAX_SIZE]; //[i] transformation index in corresponding transformation

                                        //64 (64*SIMPLE_TRANSFORMATION_MAX_SIZE)        //32+32*ALL_TRANSFORMATION_MAX_SIZE+
    SimpleTransformation simpleTransformations[SIMPLE_TRANSFORMATION_MAX_SIZE];
                                        //32 (32*COMPLEX_TRANSFORMATION_MAX_SIZE)       //32+32*ALL_TRANSFORMATION_MAX_SIZE+64*SIMPLE_TRANSFORMATION_MAX_SIZE
    ComplexTransformation complexTransformations[COMPLEX_TRANSFORMATION_MAX_SIZE];
                            //размер:   //32     + 32*ALL_TRANSFORMATION_MAX_SIZE + 64*SIMPLE_TRANSFORMATION_MAX_SIZE + 176 + 64*COMPLEX_TRANSFORMATION_MATRICES_MAX_SIZE
};

uniform int isPerson;
uniform mat4 model;

uniform mat4 rotationMatrices[120]; // complex rotation matrices

float value_to_0_2PI_diapazon(float value)
{
    if (value < 0)
        return 2*M_PI-mod(-value, 2*M_PI);
    else
        return mod(value, 2*M_PI);
}

/*float value_to_0_2PI_diapazon(float value)
{
    if (value < 0)
        return 2*M_PI - value;
    return value;
}*/

mat4 createComplexTransformationMatrix(ComplexTransformation complexTransformation)
{
    //calculating distance from object vertex to person location with projection to direction axes
    vec3 dir = complexTransformation.direction;
    float dist = dot(personLocation - position, dir) / length(dir);
    //calculating rotation angle
    float angle = 2 * M_PI * dist / complexTransformation.distance * complexTransformation.coefficient;

    int index = int(value_to_0_2PI_diapazon(angle) / 2 / M_PI * COMPLEX_TRANSFORMATION_MATRICES_MAX_SIZE);




    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    /*//calculating distance from object vertex to person location with projection to direction axes
    float integerPart = 0;
    vec3 dir = complexTransformation.direction;
    float dist = dot(personLocation - position, dir) / length(dir);
    //calculating rotation angle
    //float angle = 2 * M_PI * dist / complexTransformation.distance * complexTransformation.coefficient;
    int index = int(abs(dist) / complexTransformation.distance * complexTransformation.coefficient * COMPLEX_TRANSFORMATION_MATRICES_MAX_SIZE);
    //int index = int(personLocation.z * complexTransformation.coefficient);*/
    mat4 result = complexTransformation.offsetTranslate;
    if (index >= 0 && index < COMPLEX_TRANSFORMATION_MATRICES_MAX_SIZE)
        result = result * rotationMatrices[index];
    else
<<<<<<< HEAD
        return complexTransformation.rotationMatrices[COMPLEX_TRANSFORMATION_MATRICES_MAX_SIZE/2];*/









    float ca = cos(angle);
=======
        result = result * mat4(0);
    result = result * complexTransformation.offsetTranslateNegative;
    return result;



    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    /*float ca = cos(angle);
>>>>>>> master
    float sa = sin(angle);
    //calculating rotation matrix around direction axis on angle
    mat4 result = complexTransformation.offsetTranslate;
    result = result * mat4(ca + (1 - ca) * dir.x * dir.x, (1 - ca) * dir.y * dir.x + sa * dir.z, (1 - ca) * dir.z * dir.x - sa * dir.y, 0,
                       (1 - ca) * dir.x * dir.y - sa * dir.z, ca + (1 - ca) * dir.y * dir.y, (1 - ca) * dir.z * dir.y + sa * dir.x, 0,
                       (1 - ca) * dir.x * dir.z + sa * dir.y, (1 - ca) * dir.y * dir.z - sa * dir.x, ca + (1 - ca) * dir.z * dir.z, 0,
                                                           0,                                     0,                             0, 1);
    result = result * complexTransformation.offsetTranslateNegative;
    return result;
}

mat4 calculateTransformations()
{
    //scale -> translate -> translate_back
    mat4 result = mat4(1.0);
    //for (int i = countTransformations - 1; i >= 0; i--) //reverse is wrong
    for (int i = 0; i < countTransformations; i++) //direct is right
        if (transformationsTypes[i] == SIMPLE_MATRIX_TYPE)
            result = result * simpleTransformations[transformationsQueue[i]].transformationMatrix;
        else if (transformationsTypes[i] == COMPLEX_ROTATION_TYPE)
            result = result * createComplexTransformationMatrix(complexTransformations[transformationsQueue[i]]);
    return result;
}

void main()
{
    mat4 calculatedTransformations = calculateTransformations();
    vec4 mvPos = view * calculateTransformations() * model * vec4(position, 1.0);
    if (isPerson == 0)
        gl_Position = projection * mvPos; // drawing objects
    else if (isPerson == 1)
        gl_Position = projection * model * vec4(position, 1.0); // drawing person
    fragTextureCoord = textureCoord;
    fragMVVertexNormal = normalize(view * calculateTransformations() * model * vec4(vertexNormal, 0.0)).xyz;
    fragMVVertexPos = mvPos.xyz;
}