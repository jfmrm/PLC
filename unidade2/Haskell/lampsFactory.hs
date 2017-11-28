module Main where

import Control.Concurrent
import Control.Concurrent.MVar

makeResorce :: MVar Int -> IO()
makeResorce resource =
  do
    r <- takeMVar resource
    putMVar resource(r+1)
    putStrLn("Feito um recurso")

makeLamp :: MVar Int -> MVar Int -> MVar Int -> MVar Int -> IO()
makeLamp sockets bulbs embalagem lamps =
  do
    s <- takeMVar sockets
    b <- takeMVar bulbs
    l <- takeMVar lamps
    e <- takeMVar embalagem

    putMVar sockets(s-1)
    putMVar bulbs(b-1)
    putMVar embalagem(e-1)
    putMVar lamps(l+1)
    putStrLn("lampada Feita")

encaixota :: MVar Int -> MVar Int -> IO()
encaixota lamps boxes =
  do
    l <- takeMVar lamps

    if l == 50 then
      do
        b <- takeMVar boxes
        putMVar boxes(b+1)
        putMvar lamps(0)
        putStrLn("caixa fechada")
    else
      putMVar lamps(l)
      putStrLn("lampada encaixotada")

main :: IO()
main =
  do
    bulbs <- newMVar 0
    sockets <- newMVar 0
    embalagem <- newMVar 0
    boxes <- newMVar 0
    lamps <- newMVar 0

    forkIO(makeResorce bulbs)
    forkIO(makeResorce sockets)
    forkIO(makeResorce embalagem)
    forkIO(makeLamp sockets bulbs embalagem lamps)
    forkIO(encaixota lamps boxes)

    return()
