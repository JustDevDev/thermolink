import { useEffect, useCallback } from 'react';

/**
 * Hook that triggers the provided callback function when Enter key is pressed
 * @param callback Function to be called when Enter key is pressed
 */
export const useEnterKeySubmit = (callback: () => Promise<void> | void) => {
  const handleKeyDown = useCallback(
    (event: KeyboardEvent) => {
      if (event.key === 'Enter') {
        callback();
      }
    },
    [callback]
  );

  useEffect(() => {
    document.addEventListener('keydown', handleKeyDown);
    return () => {
      document.removeEventListener('keydown', handleKeyDown);
    };
  }, [handleKeyDown]);
};