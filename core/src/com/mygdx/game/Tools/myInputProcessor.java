// package com.mygdx.game.Tools;

// import com.badlogic.gdx.Gdx;
// import com.badlogic.gdx.Input;
// import com.badlogic.gdx.InputProcessor;
// import com.badlogic.gdx.Input.Keys;
// import com.badlogic.gdx.math.Vector2;
// import com.badlogic.gdx.physics.box2d.World;
// import com.mygdx.game.Screens.PlayScreen;
// import com.mygdx.game.Sprites.Hero;

// public class myInputProcessor implements InputProcessor {

//     private Hero hero;
//     private boolean isAttacking;
//     private float attackTimer;
//     private boolean oneDirection = false;
//     private int touchCounter = 0;

//     public myInputProcessor(Hero hero) {
//        this.hero = hero;
//     }

//     @Override
//     public boolean keyDown(int keycode) {

//         switch (keycode)
// 	    {
//             case Keys.A:
//                 // hero.setLeftMove(true);
//                 hero.moveLeft(true);
//                 break;
//             case Keys.D:
//                 // hero.setRightMove(true);
//                 hero.moveRight(true);
//                 break;
//             case Keys.SPACE:
//                 hero.jump();
//                 break;
// 	    }

//         return true;
//     }

//     @Override
//     public boolean keyUp(int keycode) {
//         switch (keycode)
// 	    {
//             case Keys.A:
//                 hero.setLeftMove(false);
//                 hero.stopMoving();
//                 break;
//             case Keys.D:
//                 hero.setRightMove(false);
//                 hero.stopMoving();
//                 break;
//             case Keys.SPACE:
//                 hero.jump();
//                 break;
// 	    }

//         return true;
//     }

//     @Override
//     public boolean touchDown(int screenX, int screenY, int pointer, int button) {
//         if (button == Input.Buttons.LEFT && !isAttacking) {
//             hero.attack();
//             attackTimer = 0f;
//             isAttacking = true;
//         }
//         return false;
//     }

//     @Override
//     public boolean touchUp(int screenX, int screenY, int pointer, int button) {
//         return false;
//     }

//     @Override
//     public boolean touchDragged(int screenX, int screenY, int pointer) {
//         return false;
//     }

//     @Override
//     public boolean mouseMoved(int screenX, int screenY) {
//         return false;
//     }

//     // 
//     public void update(float delta) {
//         if (isAttacking) {
//             attackTimer += delta;
//             if (attackTimer >= hero.getAttackDuration()) {
//                 isAttacking = false;
//                 attackTimer = 0f;
//             }
//         }
//     }

//     @Override
//     public boolean keyTyped(char character) {
//         return false;
//     }

//     @Override
//     public boolean scrolled(float amountX, float amountY) {
//         return false;
//     }
// }
