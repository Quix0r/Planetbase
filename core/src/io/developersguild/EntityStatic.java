package io.developersguild;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Quaternion;
import com.badlogic.gdx.math.Vector3;

public class EntityStatic {

	private Vector3 position;

	private Space parent;
	
	private final ModelInstance mainModel;
	
	public float timeHeld;
	
	public int health=10;

	public EntityStatic(ModelInstance mainModel, float x, float y, float z){
		this.mainModel=mainModel;
		this.position=new Vector3(x,y,z).nor();
		mainModel.transform.set(position, new Quaternion().setFromCross(new Vector3(0,0,1), position));
	}
	
	public void renderEntity(ModelBatch batch, Environment env){
		batch.render(mainModel, env);
	}
	
	public void renderGui(ModelBatch batch, Environment env, Camera cam, float deltaTime){
		//guiModel.transform.set(position, new Quaternion().setFromCross(new Vector3(0,1,0), getHeading(cam)));

		/*
		Vector3 heading=getHeading(cam);
		guiModel.transform.set(new Vector3(position).scl(1.05f),
				new Quaternion()
				.setFromCross(new Vector3(1,0,0), heading)
				);
		
		//guiModel.transform.rotate(heading, (float) Math.acos(new Vector3(1,0,0).dot(heading))*180/MathUtils.PI);
		batch.render(guiModel, env);
		*/
	}
	
	
	public Vector3 getHeading(Camera viewpoint){
		//rejection of up onto position. In the plane of up and position, and perpendicular to position.
		return new Vector3(viewpoint.up).sub(proj(viewpoint.up, position)).nor();
	}
	
	private Vector3 proj(Vector3 length, Vector3 axis){
		return new Vector3(axis).scl(axis.dot(length));
	}
	
	public void handleGuiInteraction(float deltaTime, Camera cam) {
		if(Gdx.input.isKeyPressed(Keys.F)) {
			timeHeld += deltaTime;
		} else if(timeHeld != 0) {
			Trajectory trajectory=new Trajectory(position,  getHeading(cam), timeHeld);
			float[] endpoint=trajectory.getEndpoint(1.0f);
			StrategyGame.INSTANCE.createEntity(endpoint[0], endpoint[1], endpoint[2]);
			timeHeld=0;
		}
	}

	public double x(){return position.x;}
	public double y(){return position.y;}
	public double z(){return position.z;}

	/**
	 * @return the parent
	 */
	public Space getParent() {
		return parent;
	}

	/**
	 * @param parent the parent to set
	 */
	public void setParent(Space parent) {
		this.parent = parent;
	}
	
	public boolean canSelect(){
		return true;
	}

	public void select(){
		this.mainModel.materials.get(0).set(StrategyGame.blackAttr);
	}
	
	public void deselect() {
		this.mainModel.materials.get(0).set(StrategyGame.blueAttr);
	}
	
	public void damage(int damage){
		health -= damage;
		if(health < 0){
			parent.removeEntity(this);
		}
	}
	
}
