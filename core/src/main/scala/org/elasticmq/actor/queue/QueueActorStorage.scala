package org.elasticmq.actor.queue

import akka.actor.ActorRef
import org.elasticmq.util.NowProvider
import org.elasticmq.{FifoDeduplicationIdsHistory, QueueData}

trait QueueActorStorage {
  def nowProvider: NowProvider

  def initialQueueData: QueueData

  var deadLettersActorRef: Option[ActorRef]
  def copyMessagesToActorRef: Option[ActorRef]
  def moveMessagesToActorRef: Option[ActorRef]

  def nextSequenceNumber(): BigInt = {
    val x = sequenceNumber
    sequenceNumber = sequenceNumber + 1
    x
  }

  var queueData: QueueData = initialQueueData
  var messageQueue: MessageQueue = MessageQueue(queueData.isFifo)
  var fifoMessagesHistory: FifoDeduplicationIdsHistory = FifoDeduplicationIdsHistory.newHistory()
  val receiveRequestAttemptCache = new ReceiveRequestAttemptCache
  var sequenceNumber = BigInt(0)
}
