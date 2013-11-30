package com.example.game;

import com.example.ball.R;

import android.app.Activity;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends Activity {
	MediaPlayer mpMusic;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		View game = new Game(this, null);
		setContentView(game);
		
		mpMusic = new MediaPlayer();
		mpMusic.reset();
		
		mpMusic = MediaPlayer.create(this, R.raw.bgm);
		mpMusic.start();
	}
}