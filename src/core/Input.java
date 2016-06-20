//	The MIT License (MIT)
//	
//	Copyright (c) 2016 Tzu-Chieh Chang (as known as D01phiN)
//	
//	Permission is hereby granted, free of charge, to any person obtaining a copy
//	of this software and associated documentation files (the "Software"), to deal
//	in the Software without restriction, including without limitation the rights
//	to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
//	copies of the Software, and to permit persons to whom the Software is
//	furnished to do so, subject to the following conditions:
//	
//	The above copyright notice and this permission notice shall be included in all
//	copies or substantial portions of the Software.
//	
//	THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
//	IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
//	FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
//	AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
//	LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
//	OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
//	SOFTWARE.

package core;

import java.awt.KeyboardFocusManager;
import java.util.HashMap;
import java.util.Map;

public final class Input
{
	public static final Map<Integer, String> KEY_CHAR_MAP = new HashMap<>();
	
	public static final int NUM_KEYCODES     = 256;
	public static final int NUM_MOUSEBUTTONS = 5;
	
	public static final int MOUSE_LEFT          = 0x00;
	public static final int MOUSE_RIGHT         = 0x01;
	public static final int MOUSE_MIDDLE        = 0x02;
	
	public static final int KEY_NONE            = 0x00;
	public static final int KEY_ESCAPE          = 0x01;
	public static final int KEY_1               = 0x02;
	public static final int KEY_2               = 0x03;
	public static final int KEY_3               = 0x04;
	public static final int KEY_4               = 0x05;
	public static final int KEY_5               = 0x06;
	public static final int KEY_6               = 0x07;
	public static final int KEY_7               = 0x08;
	public static final int KEY_8               = 0x09;
	public static final int KEY_9               = 0x0A;
	public static final int KEY_0               = 0x0B;
	public static final int KEY_MINUS           = 0x0C; /* - on main keyboard */
	public static final int KEY_EQUALS          = 0x0D;
	public static final int KEY_BACK            = 0x0E; /* backspace */
	public static final int KEY_TAB             = 0x0F;
	public static final int KEY_Q               = 0x10;
	public static final int KEY_W               = 0x11;
	public static final int KEY_E               = 0x12;
	public static final int KEY_R               = 0x13;
	public static final int KEY_T               = 0x14;
	public static final int KEY_Y               = 0x15;
	public static final int KEY_U               = 0x16;
	public static final int KEY_I               = 0x17;
	public static final int KEY_O               = 0x18;
	public static final int KEY_P               = 0x19;
	public static final int KEY_LBRACKET        = 0x1A;
	public static final int KEY_RBRACKET        = 0x1B;
	public static final int KEY_RETURN          = 0x1C; /* Enter on main keyboard */
	public static final int KEY_LCONTROL        = 0x1D;
	public static final int KEY_A               = 0x1E;
	public static final int KEY_S               = 0x1F;
	public static final int KEY_D               = 0x20;
	public static final int KEY_F               = 0x21;
	public static final int KEY_G               = 0x22;
	public static final int KEY_H               = 0x23;
	public static final int KEY_J               = 0x24;
	public static final int KEY_K               = 0x25;
	public static final int KEY_L               = 0x26;
	public static final int KEY_SEMICOLON       = 0x27;
	public static final int KEY_APOSTROPHE      = 0x28;
	public static final int KEY_GRAVE           = 0x29; /* ~, tilde key, accent grave */
	public static final int KEY_LSHIFT          = 0x2A;
	public static final int KEY_BACKSLASH       = 0x2B;
	public static final int KEY_Z               = 0x2C;
	public static final int KEY_X               = 0x2D;
	public static final int KEY_C               = 0x2E;
	public static final int KEY_V               = 0x2F;
	public static final int KEY_B               = 0x30;
	public static final int KEY_N               = 0x31;
	public static final int KEY_M               = 0x32;
	public static final int KEY_COMMA           = 0x33;
	public static final int KEY_PERIOD          = 0x34; /* . on main keyboard */
	public static final int KEY_SLASH           = 0x35; /* / on main keyboard */
	public static final int KEY_RSHIFT          = 0x36;
	public static final int KEY_MULTIPLY        = 0x37; /* * on numeric keypad */
	public static final int KEY_LMENU           = 0x38; /* left Alt */
	public static final int KEY_LALT            = KEY_LMENU; /* left Alt */
	public static final int KEY_SPACE           = 0x39;
	public static final int KEY_CAPITAL         = 0x3A;
	public static final int KEY_F1              = 0x3B;
	public static final int KEY_F2              = 0x3C;
	public static final int KEY_F3              = 0x3D;
	public static final int KEY_F4              = 0x3E;
	public static final int KEY_F5              = 0x3F;
	public static final int KEY_F6              = 0x40;
	public static final int KEY_F7              = 0x41;
	public static final int KEY_F8              = 0x42;
	public static final int KEY_F9              = 0x43;
	public static final int KEY_F10             = 0x44;
	public static final int KEY_NUMLOCK         = 0x45;
	public static final int KEY_SCROLL          = 0x46; /* Scroll Lock */
	public static final int KEY_NUMPAD7         = 0x47;
	public static final int KEY_NUMPAD8         = 0x48;
	public static final int KEY_NUMPAD9         = 0x49;
	public static final int KEY_SUBTRACT        = 0x4A; /* - on numeric keypad */
	public static final int KEY_NUMPAD4         = 0x4B;
	public static final int KEY_NUMPAD5         = 0x4C;
	public static final int KEY_NUMPAD6         = 0x4D;
	public static final int KEY_ADD             = 0x4E; /* + on numeric keypad */
	public static final int KEY_NUMPAD1         = 0x4F;
	public static final int KEY_NUMPAD2         = 0x50;
	public static final int KEY_NUMPAD3         = 0x51;
	public static final int KEY_NUMPAD0         = 0x52;
	public static final int KEY_DECIMAL         = 0x53; /* . on numeric keypad */
	public static final int KEY_F11             = 0x57;
	public static final int KEY_F12             = 0x58;
	public static final int KEY_F13             = 0x64; /*                     (NEC PC98) */
	public static final int KEY_F14             = 0x65; /*                     (NEC PC98) */
	public static final int KEY_F15             = 0x66; /*                     (NEC PC98) */
	public static final int KEY_KANA            = 0x70; /* (Japanese keyboard)            */
	public static final int KEY_CONVERT         = 0x79; /* (Japanese keyboard)            */
	public static final int KEY_NOCONVERT       = 0x7B; /* (Japanese keyboard)            */
	public static final int KEY_YEN             = 0x7D; /* (Japanese keyboard)            */
	public static final int KEY_NUMPADEQUALS    = 0x8D; /* = on numeric keypad (NEC PC98) */
	public static final int KEY_CIRCUMFLEX      = 0x90; /* (Japanese keyboard)            */
	public static final int KEY_AT              = 0x91; /*                     (NEC PC98) */
	public static final int KEY_COLON           = 0x92; /*                     (NEC PC98) */
	public static final int KEY_UNDERLINE       = 0x93; /*                     (NEC PC98) */
	public static final int KEY_KANJI           = 0x94; /* (Japanese keyboard)            */
	public static final int KEY_STOP            = 0x95; /*                     (NEC PC98) */
	public static final int KEY_AX              = 0x96; /*                     (Japan AX) */
	public static final int KEY_UNLABELED       = 0x97; /*                        (J3100) */
	public static final int KEY_NUMPADENTER     = 0x9C; /* Enter on numeric keypad */
	public static final int KEY_RCONTROL        = 0x9D;
	public static final int KEY_NUMPADCOMMA     = 0xB3; /* , on numeric keypad (NEC PC98) */
	public static final int KEY_DIVIDE          = 0xB5; /* / on numeric keypad */
	public static final int KEY_SYSRQ           = 0xB7;
	public static final int KEY_RMENU           = 0xB8; /* right Alt */
	public static final int KEY_RALT            = KEY_RMENU; /* right Alt */
	public static final int KEY_PAUSE           = 0xC5; /* Pause */
	public static final int KEY_HOME            = 0xC7; /* Home on arrow keypad */
	public static final int KEY_UP              = 0xC8; /* UpArrow on arrow keypad */
	public static final int KEY_PRIOR           = 0xC9; /* PgUp on arrow keypad */
	public static final int KEY_LEFT            = 0xCB; /* LeftArrow on arrow keypad */
	public static final int KEY_RIGHT           = 0xCD; /* RightArrow on arrow keypad */
	public static final int KEY_END             = 0xCF; /* End on arrow keypad */
	public static final int KEY_DOWN            = 0xD0; /* DownArrow on arrow keypad */
	public static final int KEY_NEXT            = 0xD1; /* PgDn on arrow keypad */
	public static final int KEY_INSERT          = 0xD2; /* Insert on arrow keypad */
	public static final int KEY_DELETE          = 0xD3; /* Delete on arrow keypad */
	public static final int KEY_LMETA           = 0xDB; /* Left Windows/Option key */
	public static final int KEY_LWIN            = KEY_LMETA; /* Left Windows key */
	public static final int KEY_RMETA           = 0xDC; /* Right Windows/Option key */
	public static final int KEY_RWIN            = KEY_RMETA; /* Right Windows key */
	public static final int KEY_APPS            = 0xDD; /* AppMenu key */
	public static final int KEY_POWER           = 0xDE;
	public static final int KEY_SLEEP           = 0xDF;
	
	private static boolean[] lastKeyStates    = new boolean[NUM_KEYCODES];
	private static boolean[] currentKeyStates = new boolean[NUM_KEYCODES];
	
	private static KeyEventPublisher keyEventPublisher;
	
	public static void init()
	{
		for(int keyCode = 0; keyCode < NUM_KEYCODES; keyCode++)
		{
			lastKeyStates[keyCode]    = false;
			currentKeyStates[keyCode] = false;
		}
		
		keyEventPublisher = new KeyEventPublisher();
		KeyboardFocusManager.getCurrentKeyboardFocusManager().addKeyEventDispatcher(keyEventPublisher);
		
		KEY_CHAR_MAP.put(KEY_A, "A");
		KEY_CHAR_MAP.put(KEY_B, "B");
		KEY_CHAR_MAP.put(KEY_C, "C");
		KEY_CHAR_MAP.put(KEY_D, "D");
		KEY_CHAR_MAP.put(KEY_E, "E");
		KEY_CHAR_MAP.put(KEY_F, "F");
		KEY_CHAR_MAP.put(KEY_G, "G");
		KEY_CHAR_MAP.put(KEY_H, "H");
		KEY_CHAR_MAP.put(KEY_I, "I");
		KEY_CHAR_MAP.put(KEY_J, "J");
		KEY_CHAR_MAP.put(KEY_K, "K");
		KEY_CHAR_MAP.put(KEY_L, "L");
		KEY_CHAR_MAP.put(KEY_M, "M");
		KEY_CHAR_MAP.put(KEY_N, "N");
		KEY_CHAR_MAP.put(KEY_O, "O");
		KEY_CHAR_MAP.put(KEY_P, "P");
		KEY_CHAR_MAP.put(KEY_Q, "Q");
		KEY_CHAR_MAP.put(KEY_R, "R");
		KEY_CHAR_MAP.put(KEY_S, "S");
		KEY_CHAR_MAP.put(KEY_T, "T");
		KEY_CHAR_MAP.put(KEY_U, "U");
		KEY_CHAR_MAP.put(KEY_V, "V");
		KEY_CHAR_MAP.put(KEY_W, "W");
		KEY_CHAR_MAP.put(KEY_X, "X");
		KEY_CHAR_MAP.put(KEY_Y, "Y");
		KEY_CHAR_MAP.put(KEY_Z, "Z");
		
		KEY_CHAR_MAP.put(KEY_F1, "F1");
		KEY_CHAR_MAP.put(KEY_F2, "F2");
		KEY_CHAR_MAP.put(KEY_F3, "F3");
		KEY_CHAR_MAP.put(KEY_F4, "F4");
		KEY_CHAR_MAP.put(KEY_F5, "F5");
		KEY_CHAR_MAP.put(KEY_F6, "F6");
		KEY_CHAR_MAP.put(KEY_F7, "F7");
		KEY_CHAR_MAP.put(KEY_F8, "F8");
		KEY_CHAR_MAP.put(KEY_F9, "F9");
		KEY_CHAR_MAP.put(KEY_F10, "F10");
		KEY_CHAR_MAP.put(KEY_F11, "F11");
		KEY_CHAR_MAP.put(KEY_F12, "F12");
	}
	
	public synchronized static void update()
	{
		for(int keyCode = 0; keyCode < NUM_KEYCODES; keyCode++)
		{
			lastKeyStates[keyCode] = currentKeyStates[keyCode];
		}
		
		keyEventPublisher.pollKeyStates(currentKeyStates);
	}
	
	public synchronized static boolean keyHold(int keyCode)
	{
		return currentKeyStates[keyCode] && lastKeyStates[keyCode];
	}
	
	public synchronized static boolean keyDown(int keyCode)
	{
		return currentKeyStates[keyCode] && !lastKeyStates[keyCode];
	}
	
	public synchronized static boolean keyUp(int keyCode)
	{
		return !currentKeyStates[keyCode] && lastKeyStates[keyCode];
	}
}
