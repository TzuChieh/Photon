package core;

import java.awt.KeyEventDispatcher;
import java.awt.event.KeyEvent;
import java.util.HashMap;
import java.util.Map;

import util.Debug;

public class KeyEventPublisher implements KeyEventDispatcher
{
	public static final int STATE_EMPTY = 0;
	public static final int STATE_UP    = 1;
	public static final int STATE_DOWN  = 2;
	
	private Map<Integer, Integer> m_keyMap;
	
	private int[] m_internalKeyStates;
	
	public KeyEventPublisher()
	{
		m_internalKeyStates = new int[Input.NUM_KEYCODES];
		for(int i = 0; i < Input.NUM_KEYCODES; i++)
		{
			m_internalKeyStates[i] = STATE_EMPTY;
		}
		
		m_keyMap = new HashMap<>();
		
		m_keyMap.put(KeyEvent.VK_A, Input.KEY_A);
		m_keyMap.put(KeyEvent.VK_B, Input.KEY_B);
		m_keyMap.put(KeyEvent.VK_C, Input.KEY_C);
		m_keyMap.put(KeyEvent.VK_D, Input.KEY_D);
		m_keyMap.put(KeyEvent.VK_E, Input.KEY_E);
		m_keyMap.put(KeyEvent.VK_F, Input.KEY_F);
		m_keyMap.put(KeyEvent.VK_G, Input.KEY_G);
		m_keyMap.put(KeyEvent.VK_H, Input.KEY_H);
		m_keyMap.put(KeyEvent.VK_I, Input.KEY_I);
		m_keyMap.put(KeyEvent.VK_J, Input.KEY_J);
		m_keyMap.put(KeyEvent.VK_K, Input.KEY_K);
		m_keyMap.put(KeyEvent.VK_L, Input.KEY_L);
		m_keyMap.put(KeyEvent.VK_M, Input.KEY_M);
		m_keyMap.put(KeyEvent.VK_N, Input.KEY_N);
		m_keyMap.put(KeyEvent.VK_O, Input.KEY_O);
		m_keyMap.put(KeyEvent.VK_P, Input.KEY_P);
		m_keyMap.put(KeyEvent.VK_Q, Input.KEY_Q);
		m_keyMap.put(KeyEvent.VK_R, Input.KEY_R);
		m_keyMap.put(KeyEvent.VK_S, Input.KEY_S);
		m_keyMap.put(KeyEvent.VK_T, Input.KEY_T);
		m_keyMap.put(KeyEvent.VK_U, Input.KEY_U);
		m_keyMap.put(KeyEvent.VK_V, Input.KEY_V);
		m_keyMap.put(KeyEvent.VK_W, Input.KEY_W);
		m_keyMap.put(KeyEvent.VK_X, Input.KEY_X);
		m_keyMap.put(KeyEvent.VK_Y, Input.KEY_Y);
		m_keyMap.put(KeyEvent.VK_Z, Input.KEY_Z);
		
		m_keyMap.put(KeyEvent.VK_F1, Input.KEY_F1);
		m_keyMap.put(KeyEvent.VK_F2, Input.KEY_F2);
		m_keyMap.put(KeyEvent.VK_F3, Input.KEY_F3);
		m_keyMap.put(KeyEvent.VK_F4, Input.KEY_F4);
		m_keyMap.put(KeyEvent.VK_F5, Input.KEY_F5);
		m_keyMap.put(KeyEvent.VK_F6, Input.KEY_F6);
		m_keyMap.put(KeyEvent.VK_F7, Input.KEY_F7);
		m_keyMap.put(KeyEvent.VK_F8, Input.KEY_F8);
		m_keyMap.put(KeyEvent.VK_F9, Input.KEY_F9);
		m_keyMap.put(KeyEvent.VK_F10, Input.KEY_F10);
		m_keyMap.put(KeyEvent.VK_F11, Input.KEY_F11);
		m_keyMap.put(KeyEvent.VK_F12, Input.KEY_F12);
		
		m_keyMap.put(KeyEvent.VK_UP, Input.KEY_UP);
		m_keyMap.put(KeyEvent.VK_DOWN, Input.KEY_DOWN);
		m_keyMap.put(KeyEvent.VK_LEFT, Input.KEY_LEFT);
		m_keyMap.put(KeyEvent.VK_RIGHT, Input.KEY_RIGHT);
	}
	
	@Override
	public boolean dispatchKeyEvent(KeyEvent e)
	{
		Integer keyCode = m_keyMap.get(e.getKeyCode());
		
//		if(e.getID() == KeyEvent.KEY_TYPED)
//		{
//			Debug.print("omg");
//		}
		
		// TODO: handle KEY_TYPED event
		// keyPressed - when the key goes down
		// keyReleased - when the key comes up
		// keyTyped - when the unicode character represented by this key is sent by the keyboard to system input.
		
		if(keyCode != null)
		{
			synchronized(m_internalKeyStates)
			{
				if(e.getID() == KeyEvent.KEY_PRESSED)
					m_internalKeyStates[keyCode] = STATE_DOWN;
				else if(e.getID() == KeyEvent.KEY_RELEASED)
					m_internalKeyStates[keyCode] = STATE_UP;
				else
					return false;
			}
			
			return true;
		}
		else
		{
//			Debug.print("keyCode = " + e.getKeyChar() + " not mapped");
		}
		
		return false;
	}
	
	public void pollKeyStates(boolean[] resultKeyStates)
	{
		synchronized(m_internalKeyStates)
		{
			for(int i = 0; i < Input.NUM_KEYCODES; i++)
			{
				if(m_internalKeyStates[i] == STATE_DOWN)
				{
					resultKeyStates[i] = true;
				}
				else if(m_internalKeyStates[i] == STATE_UP)
				{
					resultKeyStates[i] = true;
					m_internalKeyStates[i] = STATE_EMPTY;
				}
				else
				{
					resultKeyStates[i] = false;
					m_internalKeyStates[i] = STATE_EMPTY;
				}
			}
		}
	}
}
