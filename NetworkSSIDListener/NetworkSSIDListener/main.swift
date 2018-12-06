//
//  main.swift
//  NetworkSSIDListener
//
//  Created by Schoderer, Michael on 2018-12-06.
//  Copyright © 2018 devapp. All rights reserved.
//


import Foundation
import CoreWLAN
import SecurityFoundation


class SidChangeListener {
    var currentInterface: CWInterface
    var interfacesNames: [String] = []
    let client = CWWiFiClient.shared()

    // Failable init using default interface
    init?() {
        if let defaultInterface = client.interface() {
            self.currentInterface = defaultInterface
            self.client.delegate = self
        } else {
            return nil
        }
    }
    var ssid: String {
        return currentInterface.ssid() ?? "Not in WLAN"
    }

    func listen(delegate: CWEventDelegate) {
        do {
            self.client.delegate = delegate
            try self.client.startMonitoringEvent(with: .ssidDidChange)
        } catch {
            print("Start error: \(error)")
        }

    }

    func stop() {
        do {
            try self.client.stopMonitoringAllEvents()
        } catch {
            print("Stop error: \(error)")
        }
    }

}

extension SidChangeListener: CWEventDelegate {
    func ssidDidChangeForWiFiInterface(withName interfaceName: String) {
        if let ssid = self.currentInterface.ssid() {
            print("Intf (\(interfaceName)) SSID has changed , current SSID is: \(ssid)")
        }
    }
}


let wifi = SidChangeListener()
wifi?.listen(delegate: wifi!)
while true {
    sleep(1)
}

