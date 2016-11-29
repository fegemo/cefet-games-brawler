package br.cefetmg.games.player;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.MapObjects;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

/**
 *
 * @author fegemo <coutinho@decom.cefetmg.br>
 */
public class Avatar {

    public final Sprite sprite;
    private final Animation walking, jumping, climbing, idle;
    private Animation currentAnimation;
    private float animationTime;
    private AvatarState state;
    private static final float LINEAR_SPEED = 75f;
    private static final float JUMP_IMPULSE = 175f;
    private static final float GRAVITY = -200f;
    private static final float VELOCITY_Y_SATURATION = 200f;
    private static final int FRAMES_ALLOWED_FLYING = 11;
    private boolean isGrounded;
    private int framesFlying;
    private final Vector2 velocity;
    private boolean facingRight;
    private final TiledMap map;

    public Avatar(Texture texture, TiledMap map) {
        this.framesFlying = 0;
        TextureRegion[][] frames = TextureRegion.split(texture, 16, 16);
        walking = new Animation(.2f,
                frames[1][0],
                frames[1][1],
                frames[1][2],
                frames[1][3],
                frames[1][4],
                frames[1][5]
        );
        walking.setPlayMode(Animation.PlayMode.LOOP);
        jumping = new Animation(.2f,
                frames[2][0],
                frames[2][1],
                frames[2][2]
        );
        climbing = new Animation(.2f,
                frames[4][0],
                frames[4][1],
                frames[4][2],
                frames[4][3]
        );
        climbing.setPlayMode(Animation.PlayMode.LOOP);
        idle = new Animation(.2f,
                frames[0][0],
                frames[0][1],
                frames[0][2],
                frames[0][3]
        );
        idle.setPlayMode(Animation.PlayMode.LOOP);
        currentAnimation = idle;
        state = AvatarState.IDLE;
        sprite = new Sprite(currentAnimation.getKeyFrame(0));
        this.map = map;
        velocity = new Vector2();
        facingRight = true;
    }

    public void move(float xAxis, float yAxis) {
        velocity.x = xAxis * LINEAR_SPEED;
        if (state == AvatarState.CLIMBING) {
            velocity.y = yAxis * LINEAR_SPEED;
        }
        if (velocity.x != 0) {
            // atualiza a orientação (direita, esquerda) de acordo com a 
            //velocidade
            facingRight = velocity.x > 0;
        }
    }

    public void jump() {
        checkAdherenceToStairs();
        if (isGrounded && state != AvatarState.CLIMBING) {
            velocity.y = JUMP_IMPULSE;
            changeState(AvatarState.JUMPING);
            isGrounded = false;
        }
    }

    public void climbDown() {
        checkAdherenceToStairs();
    }

    private boolean checkAdherenceToStairs() {
        Rectangle spriteRect = sprite.getBoundingRectangle();

        // collision with stairs
        MapObjects stairs = map.getLayers().get("escadas").getObjects();
        for (MapObject o : stairs) {
            Rectangle rect = ((RectangleMapObject) o).getRectangle();
            if (rect.contains(spriteRect)) {
                velocity.y = 0;
                isGrounded = false;
                changeState(AvatarState.CLIMBING);
                return true;
            }
        }
        return false;
    }

    private void checkCollisions() {
        Rectangle spriteRect = sprite.getBoundingRectangle();

        boolean previouslyGrounded = isGrounded;
        isGrounded = false;

        // collision with obstacles and platforms
        MapObjects obstacles = map.getLayers().get("obstaculos").getObjects();
        for (MapObject o : obstacles) {
            RectangleMapObject mapObject = (RectangleMapObject) o;
            Rectangle objectRect = mapObject.getRectangle();
            Rectangle intersection = new Rectangle();
            Intersector.intersectRectangles(spriteRect,
                    objectRect, intersection);
            boolean isObstacleJustAPlatform = "plataforma".equals(
                    mapObject.getProperties().get("type", String.class));

            // se está em interseção com o retângulo atual...
            if (intersection.area() != 0) {

                // colisão por cima
                if (intersection.y > spriteRect.y && velocity.y > 0) {

                    // a colisão acontece apenas se o retângulo não for uma 
                    // plataforma (porque, nesse caso, ela é "passável" por 
                    // baixo)
                    if (!isObstacleJustAPlatform) {
                        velocity.y = 0;
                        System.out.println("bateu por cima");
                        return;
                    }
                } // colisão por baixo
                else if (intersection.y + intersection.height
                        < spriteRect.y + spriteRect.height && velocity.y < 0) {

                    // para a colisão acontecer, o avatar: (a) não deve estar
                    // em uma escada, (b) não deve ser uma plataforma
                    if (state != AvatarState.CLIMBING
                            || !isObstacleJustAPlatform) {
                        // corrige a posição Y
                        sprite.setY((float) Math.ceil(
                                sprite.getY() + intersection.getHeight()));
                        isGrounded = true;
                        velocity.y = 0;
                        return;
                    }
                }

                // colisão pela direita: corrige a posição X
                if (intersection.x > spriteRect.x && velocity.x > 0) {
                    if (!isObstacleJustAPlatform) {
                        sprite.setX((float) Math.floor(
                                sprite.getX() - intersection.getWidth()));
                        velocity.x = 0;
                    }
                } // colisão pela esquerda: corrige a posição X
                else if (intersection.x + intersection.width
                        < spriteRect.x + spriteRect.width && velocity.x < 0) {
                    if (!isObstacleJustAPlatform) {
                        sprite.setX((float) Math.ceil(
                                sprite.getX() + intersection.getWidth()));
                        velocity.x = 0;
                    }
                }
            } // verifica se a sprite está tocando o obstáculo pelos pés (abaixo)
            else {
                isGrounded |= objectRect.contains(
                        spriteRect.x + spriteRect.width / 2f,
                        spriteRect.y - 1);
            }
        }
        
        // permite que o avatar ainda fique alguns quadros como "grounded", 
        // mesmo depois que ele já saiu de um obstáculo ou plataforma
        // isso deixa a mecânica do jogo mais suave, juicy para o jogador
        if (previouslyGrounded && !isGrounded) {
            if (++framesFlying < FRAMES_ALLOWED_FLYING) {
                isGrounded = true;
            } else {
                framesFlying = 0;
            }
        }
    }

    /**
     * Atualiza o quadro de animação do avatar e verifica se ele deve estar com
     * animação de andar ou parado.
     *
     * @param dt
     */
    private void updateAnimationState(float dt) {
        animationTime += dt;
        if (animationTime > 100) {
            animationTime -= 100;
        }

        // se ele estiver no chão...
        if (isGrounded) {
            // e sem velocidade x, inicia a animação de IDLE
            if (velocity.x == 0) {
                changeState(AvatarState.IDLE);
            } // senão, inicia animação de WALKING
            else {
                changeState(AvatarState.WALKING);
            }
        }
    }

    /**
     * Atualiza a posição do avatar de acordo com sua velocidade.
     *
     * @param dt
     */
    private void updatePosition(float dt) {
        sprite.setPosition(
                sprite.getX() + velocity.x * dt,
                sprite.getY() + velocity.y * dt);
    }

    /**
     * Atualiza a velocidade do avatar segundo a gravidade, mas apenas se ele
     * não estiver no chão e se não estiver em preso a uma escada.
     *
     * @param dt
     */
    private void updateVelocity(float dt) {
        // se avatar não está no chão, tampouco preso a uma escada...
        if (!isGrounded && state != AvatarState.CLIMBING) {

            // atualiza sua velocidade y com a gravidade
            velocity.y = MathUtils.clamp(velocity.y + GRAVITY * dt,
                    -VELOCITY_Y_SATURATION, VELOCITY_Y_SATURATION);
        }
    }

    /**
     * Atualiza animação, posição e estado do avatar.
     *
     * @param dt
     */
    public void update(float dt) {
        // atualiza os quadros da animação atual do avatar
        updateAnimationState(dt);

        // atualiza sua velocidade
        updateVelocity(dt);

        // se ele deve se mover no eixo X ou Y...
        if (!velocity.isZero(0.01f)) {
            // encontra sua nova posição
            updatePosition(dt);

            // verifica se está interceptando algum obstáculo ou plataforma...
            // se estiver, desfaz o movimento para uma posição válida, onde o
            // avatar está prestes a tocar no objeto
            checkCollisions();

            if (state == AvatarState.CLIMBING && !checkAdherenceToStairs()) {
                changeState(AvatarState.IDLE);
            }
        }
    }

    /**
     * Desenha o avatar.
     *
     * @param batch SpriteBatch que deve ser usada para desenho. Ela já deve ter
     * sido inicializada e o desenho (batch.begin()) já deve ter começado.
     */
    public void draw(SpriteBatch batch) {
        TextureRegion currentFrame = currentAnimation.getKeyFrame(animationTime);
        sprite.setRegion(currentFrame);
        sprite.setFlip(!facingRight, false);
        sprite.draw(batch);
    }

    public void changeState(AvatarState newState) {
        if (newState == state) {
            return;
        }
//        System.out.println("changed to newState = " + newState.toString());
        switch (newState) {
            case IDLE:
                currentAnimation = idle;
                break;
            case WALKING:
                currentAnimation = walking;
                break;
            case JUMPING:
                currentAnimation = jumping;
                break;
            case CLIMBING:
                currentAnimation = climbing;
                break;
        }
        animationTime = 0;
        state = newState;
    }

    public void setPosition(int x, int y) {
        sprite.setPosition(x, y);
    }
}
