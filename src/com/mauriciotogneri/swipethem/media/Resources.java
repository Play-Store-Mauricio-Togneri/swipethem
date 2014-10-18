package com.mauriciotogneri.swipethem.media;

import android.graphics.Color;

public class Resources
{
	public static class Colors
	{
		public static final int UP = Color.argb(255, 60, 170, 230);
		public static final int DOWN = Color.argb(255, 255, 60, 60);
		public static final int LEFT = Color.argb(255, 255, 200, 40);
		public static final int RIGHT = Color.argb(255, 100, 200, 100);
	}
	
	public static class Sounds
	{
		public static final String SOUND_ROOT = "audio/sound/";
		
		public static final String SWIPE_OK = Sounds.SOUND_ROOT + "swipe.ogg";
		public static final String SWIPE_FAIL = Sounds.SOUND_ROOT + "explosion.ogg";
	}
	
	public static class Music
	{
		private static final String MUSIC_ROOT = "audio/music/";

		public static final String MUSIC = Music.MUSIC_ROOT + "music2.ogg";
	}
}