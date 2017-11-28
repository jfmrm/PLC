module Main where

import Control.Concurrent.STM
import Control.Concurrent

type Conta = TVar Int

saque :: Conta -> Int -> STM ()
saque conta quantia =
  do
    s <- readTVar conta
    writeTVar conta (s-quantia)

deposito :: Conta -> Int -> STM ()
deposito conta valor =
  do
    saque conta (valor * (-1))

saque2 :: Conta -> Int -> STM ()
saque2 conta valor =
  do
    s <- readTVar conta
    if (s-valor) < 0 then
      retry
    else
      writeTVar conta(s-valor)

saque3 :: Conta -> Conta-> Int -> STM ()
saque3 contaA contaB valor =
  do
    orElse (saque2 contaA valor) (saque2 contaB valor)

main :: IO()
main =
  do
    contaA <- atomically(newTVar 200)
    contaB <- atomically(newTVar 300)
    forkIO(atomically(saque3 contaA contaB 300))
    v <- atomically(readTVar contaA)
    z <- atomically(readTVar contaB)
    putStrLn (show v)
    putStrLn (show z)
