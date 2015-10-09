package com.timeOfWitch.android.objects;
import static android.opengl.GLES20.GL_FLOAT;
import static android.opengl.GLES20.glEnableVertexAttribArray;
import static android.opengl.GLES20.glVertexAttribPointer;
import static com.timeOfWitch.android.Constants.BYTES_PER_FLOAT;
import static com.timeOfWitch.android.Constants.PARTICLE_START_TIME_COMPONENT_COUNT;
import static com.timeOfWitch.android.Constants.STRIDE;
import static com.timeOfWitch.android.Constants.STRIDE_PARTICLE;
import static com.timeOfWitch.android.Constants.VECTOR_COMPONENT_COUNT;
import static com.timeOfWitch.android.Constants.COLOR_COMPONENT_COUNT;
import static com.timeOfWitch.android.Constants.POSITION_COMPONENT_COUNT;
import static com.timeOfWitch.android.Constants.TOTAL_COMPONENT_COUNT;
import static com.timeOfWitch.android.Constants.STRIDE_PARTICLE;
import static android.opengl.GLES20.GL_FLOAT;
import static android.opengl.GLES20.GL_TRIANGLE_STRIP;
import static android.opengl.GLES20.glDrawArrays;
import static android.opengl.GLES20.glEnableVertexAttribArray;
import static android.opengl.GLES20.glGetAttribLocation;
import static android.opengl.GLES20.glGetUniformLocation;
import static android.opengl.GLES20.glUniform1i;
import static android.opengl.GLES20.glUniformMatrix4fv;
import static android.opengl.GLES20.glUseProgram;
import static android.opengl.GLES20.glVertexAttribPointer;
import static android.opengl.Matrix.multiplyMM;
import static android.opengl.Matrix.scaleM;
import static android.opengl.Matrix.setIdentityM;
import static android.opengl.Matrix.translateM;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import com.timeOfWitch.android.R;
import com.timeOfWitch.android.Render;
import com.timeOfWitch.android.data.VertexArray;
import com.timeOfWitch.android.util.ShaderHelper;
import com.timeOfWitch.android.util.TextResourceReader;
import com.timeOfWitch.android.util.Geometry.Point;
import com.timeOfWitch.android.util.Geometry.Vector;

import android.content.Context;
import android.graphics.Color;

public class ParticleSystem {
    // Uniform constants
    protected static final String U_MATRIX = "u_Matrix";
    protected static final String U_COLOR = "u_Color";
    protected static final String U_TEXTURE_UNIT = "u_TextureUnit";
    protected static final String U_TIME = "u_Time";    

    // Attribute constants
    protected static final String A_POSITION = "a_Position";
    protected static final String A_COLOR = "a_Color";
    protected static final String A_TEXTURE_COORDINATES = "a_TextureCoordinates";
    
    
    protected static final String A_DIRECTION_VECTOR = "a_DirectionVector";
    protected static final String A_PARTICLE_START_TIME = "a_ParticleStartTime";
    
    // Attribute locations
    private  int aPositionLocation;
    private  int aColorLocation;
    private  int aDirectionVectorLocation;
    private  int aParticleStartTimeLocation;
    private  int uTextureUnitLocation;
    // Uniform locations
    private int uMatrixLocation;
    private  int uTimeLocation;    
    
    private float[] particles;
    private final int maxParticleCount;
    private int currentParticleCount;
    private int nextParticle;
    private Point position;
    private Vector direction;
    private int color;
    private FloatBuffer buffer;
    private Context context;
    private int program;

    public ParticleSystem(Point position,Vector direction,int color, int maxParticleCount) {
        
        this.context = Render.context;
        this.maxParticleCount = maxParticleCount;
        this.position=position;
        this.direction=direction;
        this.color=color;
    }
    public void addParticles(ParticleSystem particleSystem, float currentTime, 
        int count) {        
        for (int i = 0; i < count; i++) {
            particleSystem.addParticle(position, color, direction, currentTime);
        }       
    }
    public void addParticle(Point position, int color, Vector direction,
        float particleStartTime) {                
        final int particleOffset = nextParticle * TOTAL_COMPONENT_COUNT;
        
        int currentOffset = particleOffset;        
        nextParticle++;
        
        if (currentParticleCount < maxParticleCount) {
            currentParticleCount++;
        }
        
        if (nextParticle == maxParticleCount) {
            // Start over at the beginning, but keep currentParticleCount so
            // that all the other particles still get drawn.
            nextParticle = 0;
        }  
        
        particles[currentOffset++] = position.x;
        particles[currentOffset++] = position.y;
        particles[currentOffset++] = position.z;
        
        particles[currentOffset++] = Color.red(color) / 255f;
        particles[currentOffset++] = Color.green(color) / 255f;
        particles[currentOffset++] = Color.blue(color) / 255f;
        
        particles[currentOffset++] = direction.x;
        particles[currentOffset++] = direction.y;
        particles[currentOffset++] = direction.z;             
        
        particles[currentOffset++] = particleStartTime;
        
        buffer.position(particleOffset);
        buffer.put(particles, particleOffset, TOTAL_COMPONENT_COUNT);
        buffer.position(0);
        
    }
    
    public void attachParticleSystem(){
        particles = new float[maxParticleCount * TOTAL_COMPONENT_COUNT];
        buffer = ByteBuffer
            .allocateDirect(particles.length * BYTES_PER_FLOAT)
            .order(ByteOrder.nativeOrder())
            .asFloatBuffer()
            .put(particles);
        int vertexShaderTexture = ShaderHelper.compileVertexShader(TextResourceReader.readTextFileFromResource(
            context, R.raw.sprite_vs));
        int fragmentShaderTexture =  ShaderHelper.compileFragmentShader(TextResourceReader.readTextFileFromResource(
            context, R.raw.sprite_fs));
        program = ShaderHelper.linkProgram(vertexShaderTexture, fragmentShaderTexture);
        
        buffer.position(0);        
        glVertexAttribPointer(aPositionLocation,  POSITION_COMPONENT_COUNT, GL_FLOAT, false, STRIDE_PARTICLE, buffer);
        glEnableVertexAttribArray(aPositionLocation);
        buffer.position(POSITION_COMPONENT_COUNT);

        glVertexAttribPointer(aColorLocation,  COLOR_COMPONENT_COUNT, GL_FLOAT, false, STRIDE_PARTICLE, buffer);
        glEnableVertexAttribArray(aColorLocation);
        buffer.position(POSITION_COMPONENT_COUNT+COLOR_COMPONENT_COUNT);

        glVertexAttribPointer(aDirectionVectorLocation,  VECTOR_COMPONENT_COUNT, GL_FLOAT, false, STRIDE_PARTICLE, buffer);
        glEnableVertexAttribArray(aDirectionVectorLocation);
        buffer.position(POSITION_COMPONENT_COUNT+COLOR_COMPONENT_COUNT+VECTOR_COMPONENT_COUNT);        
        
        glVertexAttribPointer(aParticleStartTimeLocation,  PARTICLE_START_TIME_COMPONENT_COUNT, GL_FLOAT, false, STRIDE_PARTICLE, buffer);
        glEnableVertexAttribArray(aParticleStartTimeLocation);

        // Retrieve uniform locations for the shader program.
        uMatrixLocation = glGetUniformLocation(program, U_MATRIX);
        uTimeLocation = glGetUniformLocation(program, U_TIME);
        uTextureUnitLocation = glGetUniformLocation(program, U_TEXTURE_UNIT);
        
        // Retrieve attribute locations for the shader program.
        aPositionLocation = glGetAttribLocation(program, A_POSITION);
        aColorLocation = glGetAttribLocation(program, A_COLOR);
        aDirectionVectorLocation = glGetAttribLocation(program, A_DIRECTION_VECTOR);
        aParticleStartTimeLocation = 
        glGetAttribLocation(program, A_PARTICLE_START_TIME);
    }
    
    public void draw(){
//        glUniformMatrix4fv(uMatrixLocation,1,false,matrix,0);
//        glUniform1f(uTimeLocation,elapsedTime); 
//        glDrawArrays(GL_POINTS, 0, currentParticleCount);
    }
}
