Question 1.2

  Le Tremblement provient probablement de bruit dans les valeur de l'un ou l'autre des capteurs
  Pour y remédier nous faisons une interpolation entre la matrix de rotation cible (donnée par le SensorManager)
  et La matrix de rotation actuelle ce qui permet adoucir les mouvements du au bruit

Question 2.2

  a) Cela permet d'économiser de la place (et de la bande passante), un float sur 32-bit serait beaucoup trop précis  pour ce qu'on
  fait (et probablement que la précision du capteur sur le device)
  
  
  b)On pourrait imaginer une charactéristique batteryLevel retournant un entier entre 0 et 100 sur un byte, supportant la lecture et les notifications (l'écriture serait absurde). 
