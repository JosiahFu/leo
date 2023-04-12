import '../../DefaultPageNav.scss';
import './IkigaiBuilder.scss';
import {Button, Input, Layout, List, Modal} from 'antd';
import {Coordinate, Ikigai} from '../../../Ikigai/Ikigai';
import {useMeasure} from '@react-hookz/web';
import {ChangeEvent, useEffect, useState} from 'react';
import {createService, partial_text_openai_prompt} from '../../../protos';
import {BarsOutlined, LoadingOutlined} from '@ant-design/icons';
import PartialTextOpenAiPromptService = partial_text_openai_prompt.PartialTextOpenAiPromptService;
import Prompt = partial_text_openai_prompt.GetSuggestionsRequest.Prompt;

const {Sider, Content} = Layout;

export function IkigaiBuilder() {
  const [ikigaiCenterPosition, setIkigaiCenterPosition] =
    useState<Coordinate | null>(null);
  const [ikigaiDistanceToCategoryCenter, setIkigaiDistanceToCategoryCenter] =
    useState(0);
  const [ikigaiCategoryDiameter, setIkigaiCategoryDiameter] = useState(0);

  const [ikigaiContainerMeasure, ikigaiContainerMeasureRef] =
    useMeasure<HTMLDivElement>();
  const [useIkigaiResizeTimeout, setUseIkigaiResizeTimeout] = useState(true);
  const [ikigaiResizeTimeoutId, setIkigaiResizeTimeoutId] = useState<
    NodeJS.Timeout | undefined
  >(undefined);

  const [lovesModalOpen, setLovesModalOpen] = useState(false);
  const [lovesValue, setLovesValue] = useState('');
  const [getRelatedSuggestionsEnabled, setGetRelatedSuggestionsEnabled] =
    useState(true);
  const [lovesSuggestions, setLovesSuggestions] = useState<string[]>([]);

  const [worldNeedsModalOpen, setWorldNeedsModalOpen] = useState(false);
  const [paidForModalOpen, setPaidForModalOpen] = useState(false);
  const [goodAtModalOpen, setGoodAtModalOpen] = useState(false);

  // Resize and reposition the Ikigai diagram to be consistent with the window.
  function updateIkigaiPosition() {
    if (
      ikigaiContainerMeasureRef.current != null &&
      ikigaiContainerMeasure != null
    ) {
      setIkigaiCenterPosition({
        x:
          ikigaiContainerMeasureRef.current.offsetLeft +
          ikigaiContainerMeasure.width / 2,
        y:
          ikigaiContainerMeasureRef.current.offsetTop +
          ikigaiContainerMeasure.height / 2,
      });
      setIkigaiCategoryDiameter(
        Math.min(ikigaiContainerMeasure.width, ikigaiContainerMeasure.height) /
          2
      );
      setIkigaiDistanceToCategoryCenter(
        (Math.min(ikigaiContainerMeasure.width, ikigaiContainerMeasure.height) /
          2) *
          0.45
      );
    }
    setUseIkigaiResizeTimeout(false);
  }

  useEffect(() => {
    // TODO: This is a terrible solution. Revisit.
    if (useIkigaiResizeTimeout) {
      if (ikigaiResizeTimeoutId != null) {
        clearTimeout(ikigaiResizeTimeoutId!);
      }
      setIkigaiResizeTimeoutId(setTimeout(updateIkigaiPosition, 500));
    } else {
      updateIkigaiPosition();
    }
  }, [ikigaiContainerMeasureRef, ikigaiContainerMeasure]);

  function onLovesUpdate() {
    console.log(lovesValue);
    setLovesModalOpen(false);
  }

  function getLovesRelatedSuggestions() {
    const partialTextOpenAiPromptService = createService(
      PartialTextOpenAiPromptService,
      'PartialTextOpenAiPromptService'
    );
    setGetRelatedSuggestionsEnabled(false);
    partialTextOpenAiPromptService
      .getSuggestions({
        partialText: lovesValue,
        prompt: Prompt.SUGGEST_THINGS_YOU_LOVE,
      })
      .then(response => setLovesSuggestions(response.suggestions))
      .catch(() => setLovesSuggestions([]))
      .finally(() => setGetRelatedSuggestionsEnabled(true));
  }

  function onWorldNeedsUpdate() {
    setWorldNeedsModalOpen(false);
  }

  function onPaidForUpdate() {
    setPaidForModalOpen(false);
  }

  function onGoodAtUpdate() {
    setGoodAtModalOpen(false);
  }

  return (
    <>
      <Layout style={{height: '100%'}}>
        <Content style={{borderRight: '#F0781F solid 1px'}}>
          <div
            style={{width: '100%', height: '100%'}}
            ref={ikigaiContainerMeasureRef}
          >
            <div className="subtitle">Ikigai Builder</div>
            <div className="brief-instructions">Click each circle to edit.</div>
            <Ikigai
              id="ikigai-builder"
              centerPosition={ikigaiCenterPosition}
              categoryDiameter={ikigaiCategoryDiameter}
              distanceToCategoryCenter={ikigaiDistanceToCategoryCenter}
              lovesResizeAndRotateElement={
                <>
                  Something you <b>LOVE</b>
                  {lovesValue.length > 0 ? (
                    <>
                      <br />
                      <span style={{fontSize: 'small'}}>{lovesValue}</span>
                    </>
                  ) : (
                    ''
                  )}
                </>
              }
              onLovesClick={() => setLovesModalOpen(true)}
              lovesValueIsSet={lovesValue.length > 0 ? 0 : 1}
              worldNeedsResizeAndRotateElement={
                <>
                  What the world <b>NEEDS</b>
                </>
              }
              onWorldNeedsClick={() => setWorldNeedsModalOpen(true)}
              paidForResizeAndRotateElement={
                <>
                  What you can be <b>PAID&nbsp;FOR</b>
                </>
              }
              onPaidForClick={() => setPaidForModalOpen(true)}
              goodAtResizeAndRotateElement={
                <>
                  What you are <b>GOOD&nbsp;AT</b>
                </>
              }
              onGoodAtClick={() => setGoodAtModalOpen(true)}
            />
          </div>
        </Content>
        <Sider reverseArrow>
          <div>Saved Projects</div>
        </Sider>
      </Layout>
      <Modal
        title="What you LOVE!"
        width="50%"
        open={lovesModalOpen}
        closable={true}
        onOk={onLovesUpdate}
        onCancel={() => setLovesModalOpen(false)}
      >
        <Input
          placeholder="What do you LOVE?"
          maxLength={255}
          onChange={(e: ChangeEvent<HTMLInputElement>) =>
            setLovesValue(e.target.value)
          }
          value={lovesValue}
          onPressEnter={onLovesUpdate}
        />
        <Button
          style={{width: '100%'}}
          onClick={getLovesRelatedSuggestions}
          icon={
            getRelatedSuggestionsEnabled ? (
              <BarsOutlined />
            ) : (
              <LoadingOutlined />
            )
          }
          disabled={!getRelatedSuggestionsEnabled}
        >
          Get Related Suggestions
        </Button>
        <List
          dataSource={lovesSuggestions}
          renderItem={suggestion => (
            <List.Item itemID={suggestion}>
              <div onClick={() => setLovesValue(suggestion)}>{suggestion}</div>
            </List.Item>
          )}
        />
      </Modal>
      <Modal
        title="What the world NEEDS!"
        width="50%"
        open={worldNeedsModalOpen}
        closable={true}
        onCancel={() => setWorldNeedsModalOpen(false)}
        onOk={onWorldNeedsUpdate}
      ></Modal>
      <Modal
        title="What you can be PAID FOR!"
        width="50%"
        open={paidForModalOpen}
        closable={true}
        onCancel={() => setPaidForModalOpen(false)}
        onOk={onPaidForUpdate}
      >
        Hello
      </Modal>
      <Modal
        title="What you are GOOD AT!"
        width="50%"
        open={goodAtModalOpen}
        closable={true}
        onCancel={() => setGoodAtModalOpen(false)}
        onOk={onGoodAtUpdate}
      >
        Hello
      </Modal>
    </>
  );
}
