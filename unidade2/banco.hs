module Main where

import Control.Concurrent.STM
import Control.Concurrent

type Conta = TVar Int

saque :: Conta -> Int -> IO()
saque conta quantia =
  do
    atomically(
      do
        s <- readTVar conta
        writeTVar conta (s-quantia))

deposito :: Conta -> Int -> IO()
deposito conta valor =
  do
    atomically(
      do
        s <- readTVar conta
        writeTVar conta (s+valor))
        
main :: IO()
main =
  do
    conta <- atomically(newTVar 200)
    forkIO(saque conta 100)
    v <- atomically(readTVar conta)
    putStrLn (show v)
