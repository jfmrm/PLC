module Main where

import Control.Concurrent
import Control.Concurrent.MVar

makeBulbo :: MVar Int -> IO()
makeBulbo bulbo =
  do
    p <- takeMVar bulbo
    putMVar bulbo(p+1)
    putStrLn("produzindo bulbo")

makeSocket :: MVar Int -> IO()
makeSocket socket =
  do
    p <- takeMVar socket
    putMVar socket(p+1)
    putStrLn("produzindo Socket")

makeEmbalagem :: MVar Int -> IO()
makeEmbalagem embalagem =
  do
    p <- takeMVar embalagem
    putMVar embalagem(p+1)
    putStrLn("produzindo embalagem")

makeLampada :: MVar Int -> MVar Int -> MVar Int -> MVar Int -> IO()
makeLampada bulbo socket lampada embalagem =
  do
    b <- takeMVar bulbo
    s <- takeMVar socket
    l <- takeMVar lampada
    e <- takeMVar embalagem

    putMVar bulbo(b-1)
    putMVar socket(s-1)
    putMVar lampada(l+1)
    putMVar embalagem(e-1)
    putStrLn("produzindo lampada")

encaixota :: MVar Int -> MVar Int -> MVar Int -> IO()
encaixota lampada caixa deposito =
  do
    l <- takeMVar lampada
    c <- takeMVar caixa

    if c >= 50
    then
      do
        putMVar caixa(0)
        c <- takeMVar caixa
        d <- takeMVar deposito
        putMVar deposito(d+1)
        putMVar lampada(l-1)
        putMVar caixa(c+1)
        putStrLn("Encaixotando lampada")
    else
      do
        putMVar lampada(l-1)
        putMVar caixa(c+1)
        putStrLn("Encaixotando lampada")

main :: IO()
main =
  do
    bulbo <- newMVar 0
    socket <- newMVar 0
    lampada <- newMVar 0
    embalagem <- newMVar 0
    caixa <- newMVar 0
    deposito <- newMVar 0

    forkIO(makeBulbo bulbo)
    forkIO(makeSocket socket)
    forkIO(makeEmbalagem embalagem)
    forkIO(makeLampada bulbo socket lampada embalagem)
    forkIO(encaixota lampada caixa deposito)

    main
    return()
